sealed class State {
    object Loading : State()
    object NotLoading : State()
    object Error : State()
}