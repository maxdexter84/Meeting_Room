package com.example.core_module.state

sealed class State {
    object Loading : State()
    object NotLoading : State()
    object Error : State()
}