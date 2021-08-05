package com.example.core_network.login_stuff

sealed class ResultOfLogIn<out T : Any> {
    data class Success<out T : Any>(val data: T) : ResultOfLogIn<T>()
    data class Error(val exception: String) : ResultOfLogIn<Nothing>()
}

