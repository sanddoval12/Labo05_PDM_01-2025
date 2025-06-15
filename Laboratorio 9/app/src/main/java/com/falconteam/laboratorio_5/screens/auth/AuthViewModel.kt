package com.falconteam.laboratorio_5.screens.auth

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falconteam.laboratorio_5.data.database.Entity.UserTokenEntity
import com.falconteam.laboratorio_5.data.database.InitDatabase
import com.falconteam.laboratorio_5.data.remote.RepositoryProvider
import com.falconteam.laboratorio_5.data.remote.resource.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed class ValidationEvent {
    data object Success: ValidationEvent()
    data class Error(val message: String): ValidationEvent()
}

class AuthViewModel() : ViewModel() {
    // Inicializar el repositorio de autenticacion
    private val authService = RepositoryProvider.authRepository

    // Inicializar la base de datos
    private val database = InitDatabase.database

    // Estado del snackbar
    val snackBarHostState = mutableStateOf(SnackbarHostState())

    // Campos de texto
    val username = mutableStateOf("")
    val password = mutableStateOf("")

    // Canal de eventos de validacion
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun showSnackBar(message: String) {
        viewModelScope.launch {
            val result = snackBarHostState.value.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            when (result) {
                SnackbarResult.Dismissed -> { }
                SnackbarResult.ActionPerformed -> { }
            }
        }
    }

    fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = authService.login(username.value, password.value)
            when(response) {
                is Resource.Error -> {
                    validationEventChannel.send(ValidationEvent.Error(response.message))
                }
                is Resource.Success -> {
                    val userTokenEntity = UserTokenEntity(
                        token = response.data.token,
                        user = username.value
                    )

                    database.authDao().insertToken(userTokenEntity)
                    validationEventChannel.send(ValidationEvent.Success)
                }
            }
        }
    }
}
