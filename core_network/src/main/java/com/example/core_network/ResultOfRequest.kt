package com.example.core_network

sealed class ResultOfRequest<out T : Any> {
    data class Success<out T : Any>(val data: T) : ResultOfRequest<T>()
    data class Error(val exception: String) : ResultOfRequest<Nothing>()
}

