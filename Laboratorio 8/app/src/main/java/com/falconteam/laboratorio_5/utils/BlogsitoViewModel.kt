package com.falconteam.laboratorio_5.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falconteam.laboratorio_5.data.database.entity.Post
import com.falconteam.laboratorio_5.data.database.InitDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class BlogsitoViewModel : ViewModel() {
    private val _listPosts = MutableStateFlow<List<Post>>(emptyList())
    private val db = InitDatabase.database
    val listPosts = _listPosts.asStateFlow()
    val showModal = mutableStateOf(false)
    val newPostTitle = mutableStateOf("")
    val newPostDescription = mutableStateOf("")

    fun showModal() {
        showModal.value = !showModal.value
    }

    private fun cleanFields() {
        newPostDescription.value = ""
        newPostTitle.value = ""
    }

    init{
        getPosts()
    }

    private fun getPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val result: List<Post>? = db.postDao().getAllPosts().firstOrNull()

            // Verificamos que no este vacio
            if (result?.isEmpty() == true) {
                return@launch
            }

            db.postDao().getAllPosts().collect { listPosts ->
                _listPosts.value = listPosts
            }
        }
    }

    fun addNewPost() {
        if (newPostDescription.value.isEmpty()) {
            return
        }

        if (newPostTitle.value.isEmpty()) {
            return
        }

        val newPost = Post(
            title = newPostTitle.value,
            description = newPostDescription.value,
            author = "EnSeMirro"
        )

        viewModelScope.launch(Dispatchers.IO) {
            db.postDao().insertPost(newPost)
            getPosts()
        }

        cleanFields()
    }

    fun updatePost(title: String, description: String, id: String) {
        viewModelScope.launch {
            db.postDao().updateSelected(title, description, id)
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            db.postDao().deletePostById(postId)
        }
    }
}