package com.meetingroom.feature_meet_now.presentation.available_now_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_module.state.State
import com.example.core_network.RequestResult
import com.meetingroom.feature_meet_now.domain.entity.Room
import com.meetingroom.feature_meet_now.data.AvailableRoomsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val NOW = "ROOMS_AVAILABLE_RIGHT_NOW"

class RoomsAvailableNowViewModel @Inject constructor(
    private val repository: AvailableRoomsRepository
) : ViewModel() {
    private val _roomsAvailableNow = MutableStateFlow<List<Room>>(emptyList())
    val roomsAvailableNow: StateFlow<List<Room>> get() = _roomsAvailableNow.asStateFlow()

    private val _loadingState = MutableStateFlow<State>(State.Loading)
    val loadingState: StateFlow<State> get() = _loadingState.asStateFlow()

    fun getRoomsAvailableNow() {
        viewModelScope.launch {
            _loadingState.emit(State.Loading)
            when (val response = repository.getAvailableRooms(NOW)) {
                is RequestResult.Success -> {
                    _roomsAvailableNow.emit(response.data)
                    _loadingState.emit(State.NotLoading)
                }
                is RequestResult.Error -> {
                    _roomsAvailableNow.emit(emptyList())
                    _loadingState.emit(State.Error)
                }
                is RequestResult.Loading -> {
                    _loadingState.emit(State.Loading)
                }
            }
        }
    }
}