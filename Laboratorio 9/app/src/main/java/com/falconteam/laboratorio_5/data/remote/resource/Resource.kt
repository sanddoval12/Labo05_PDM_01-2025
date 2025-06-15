package com.falconteam.laboratorio_5.data.remote.resource

sealed class Resource<T> {
    data class Success<T>(val data: T): Resource<T>()
    data class Error<T>(val message: String): Resource<T>()
}

data class ErrorResponse (
    val message: String
)