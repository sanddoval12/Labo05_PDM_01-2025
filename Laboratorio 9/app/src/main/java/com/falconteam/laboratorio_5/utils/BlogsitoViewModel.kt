package com.falconteam.laboratorio_5.utils

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falconteam.laboratorio_5.data.Element
import com.falconteam.laboratorio_5.data.database.Entity.PostEntity
import com.falconteam.laboratorio_5.data.database.InitDatabase
import com.falconteam.laboratorio_5.data.remote.RepositoryProvider
import com.falconteam.laboratorio_5.data.remote.resource.Resource
import com.falconteam.laboratorio_5.data.remote.services.GetAllPostWithCommentsResponse
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.URI

sealed class ValidationEvent {
    data object Success: ValidationEvent()
    data object LogOut: ValidationEvent()
    data class Error(val message: String): ValidationEvent()
}


class BlogsitoViewModel : ViewModel() {
    //Codigo para WebSocket
    private val TAG = "WssViewModel"
    private val wssConnect: MutableStateFlow<WSSClient?> = MutableStateFlow(null)
    private val _loadingWSS = MutableStateFlow(true)
    val loadingWSS = _loadingWSS.asStateFlow()
    private val reconnectDelayMillis = 5000L // 5 segundos
    private var reconnectAttempts = 0
    private val maxReconnectAttempts = 5 // Máximo de intentos de reconexión

    private val db = InitDatabase.database
    private val postService = RepositoryProvider.postRepository
    private val _listPosts = MutableStateFlow<List<PostEntity>>(emptyList())
    val listPosts = _listPosts.asStateFlow()
    val showModal = mutableStateOf(false)
    val showModalAddComment = mutableStateOf(false)
    val newPostTitle = mutableStateOf("")
    val newPostDescription = mutableStateOf("")
    val newPostComment = mutableStateOf("")
    val newPostId = mutableStateOf("")

    // Canal de eventos de validacion
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    //Codigo para WebSocket
    private val _token = MutableStateFlow<String?>(null)
    private val token = _token.asStateFlow()

    init {
        getPosts()
    }

    //Funciones para WebSocket
    fun connectWSS(token: String) {
        this._token.value = token
        Log.d(TAG, "El valor del token es: $token")

        viewModelScope.launch {
            connectWebSocket(URI("wss://dei.uca.edu.sv/moviles/wsl6/?id=$token"))
        }
    }

    private fun connectWebSocket(serverUri: URI) {
        if (wssConnect.value == null || !wssConnect.value!!.isOpen) {
            val context = this
            wssConnect.value = WSSClient(serverUri, context)
            wssConnect.value!!.connect()
        }
    }

    fun sendMessage(message: String) {
        wssConnect.value.let {
            if (it?.isOpen == true) {
                it.send(
                    message
                )
            }
            else{
                Log.d(TAG, "No se pudo enviar el mensaje: WebSocket no está conectado.")
                tryReconnectAndSend(message)
            }
        }
    }

    private fun tryReconnectAndSend(message: String) {
        if (reconnectAttempts >= maxReconnectAttempts) {
            Log.d(TAG, "Máximo de intentos de reconexión alcanzado. No se pudo enviar el mensaje.")
            return
        }

        reconnectAttempts++

        viewModelScope.launch {
            _loadingWSS.value = true
            _token.value?.let { token ->
                connectWebSocket(URI("wss://dei.uca.edu.sv/moviles/wsl6/?id=$token"))

                // Esperar un momento para que el WebSocket se conecte
                delay(reconnectDelayMillis)

                if (wssConnect.value?.isOpen == true) {
                    Log.d(TAG, "Reconexión exitosa. Enviando mensaje.")
                    wssConnect.value?.send(message)
                } else {
                    Log.d(TAG, "Reconexión fallida. Reintentando...")
                    tryReconnectAndSend(message)
                }
            } ?: Log.d(TAG, "No hay token disponible para reconectar.")
        }
    }

    fun websocketConnectionOpened(value: Boolean) {
        _loadingWSS.value = value
    }

    fun setMessageWSS(message: String) {
        val type = ObtainType(message)
        Log.d(
            TAG,
            "Mensaje recibido: $message"
        )
        when (type) {
            "NewPost" -> {
                viewModelScope.launch(Dispatchers.IO) {
                    getPosts()
                }
            }
            "NewMessage" -> {
                viewModelScope.launch(Dispatchers.IO) {
                    getPosts()
                }
            }
        }
    }

    private fun GetAllPostWithCommentsResponse.toPostEntity() : PostEntity {
        return PostEntity(_id, tittle, description, author, messages)
    }

    private fun ObtainType(message: String): String {
        val gson = Gson()
        val json = gson.fromJson(message, JsonObject::class.java)
        return json.get("type").asString
    }

    private fun parseJsonObject(jsonString: String): Element {
        val gson = Gson()
        val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)
        try {
            val type = jsonObject.get("type").asString
            val data = jsonObject.getAsJsonObject("data")
            return Element(type, data)
        } catch (
            e: Exception
        ) {
            Log.d(TAG, "Error al parsear el mensaje: $jsonString")
            return Element("Error", JsonObject().apply {
                addProperty("message", "Error al parsear el mensaje")
            })
        }
    }

    fun websocketConnectionClosed(reason: String?, code: String, remote: Boolean?) {

    }

    //Fin funciones WebSocket


    fun getPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = db.postDao().observeAll().firstOrNull()
            val getToken = db.authDao().getToken()

            if (getToken === null) {
                return@launch
            }

            val response = postService.getAllPostsWithComments(getToken.token)

            when(response) {
                is Resource.Error -> {
                    validationEventChannel.send(ValidationEvent.Error(response.message))
                }
                is Resource.Success -> {
                    val posts = response.data.map { data ->
                        data.toPostEntity()
                    }
                    db.postDao().insertAllPosts(posts)
                }
            }

            db.postDao().observeAll().collect { listPosts ->
                _listPosts.value = listPosts
            }
        }
    }

    fun showModal() {
        showModal.value = !showModal.value
    }

    fun showModalAddComment() {
        showModalAddComment.value = !showModalAddComment.value
    }

    private fun cleanFields() {
        newPostDescription.value = ""
        newPostTitle.value = ""
    }

    fun addNewPost() {
        if (newPostDescription.value.isEmpty()) {
            return
        }

        if (newPostTitle.value.isEmpty()) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val getToken = db.authDao().getToken()

            if (getToken == null) {
                validationEventChannel.send(ValidationEvent.Error("No se encontró el token de autenticación."))
                return@launch
            }

            val result = postService.addPost(
                token = getToken.token,
                title = newPostTitle.value,
                description = newPostDescription.value
            )

            when(result) {
                is Resource.Error -> {
                    validationEventChannel.send(ValidationEvent.Error(result.message))
                }
                is Resource.Success -> {

                    sendMessage(
                        Gson().toJson(
                            Element(
                                "AddPost",
                                JsonObject().apply {
                                    addProperty("title", newPostTitle.value)
                                    addProperty("description", newPostDescription.value)
                                    addProperty("author", getToken.user)
                                }
                            )
                        )
                    )

                    cleanFields()
                    getPosts()
                    validationEventChannel.send(ValidationEvent.Success)
                }
            }
        }
    }

    fun addNewComment() {
        if (newPostId.value.isEmpty()) {
            return
        }

        if (newPostComment.value.isEmpty()) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val getToken = db.authDao().getToken()

            if (getToken == null) {
                validationEventChannel.send(ValidationEvent.Error("No se encontró el token de autenticación."))
                return@launch
            }

            val result = postService.addComment(
                token = getToken.token,
                postId = newPostId.value,
                comment = newPostComment.value
            )

            when(result) {
                is Resource.Error -> {
                    validationEventChannel.send(ValidationEvent.Error(result.message))
                }
                is Resource.Success -> {
                    sendMessage(
                        Gson().toJson(
                            Element(
                                "AddMessage",
                                JsonObject().apply {
                                    addProperty("author", getToken.user)
                                    addProperty("comment", newPostComment.value)
                                    addProperty("id", newPostId.value)
                                }
                            )
                        )
                    )
                    newPostComment.value = ""
                    newPostId.value = ""
                    getPosts()
                    validationEventChannel.send(ValidationEvent.Success)
                }
            }
        }
    }

    fun logOut() {
        viewModelScope.launch(Dispatchers.Main) {
            val deleteToken = async { db.authDao().deleteAll() }
            val deletePosts = async { db.postDao().deleteAllPosts() }
            awaitAll(deleteToken, deletePosts)
            validationEventChannel.send(ValidationEvent.LogOut)
        }
    }
}