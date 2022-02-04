package com.meetingroom.feature_meet_now.presentation.available_soon_fragment

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

private const val SOON = "ROOMS_THAT_WILL_BE_AVAILABLE_WITHIN_30_MINUTES"

class RoomsAvailableSoonViewModel @Inject constructor(
    private val repository: AvailableRoomsRepository
) : ViewModel() {
    var roomsFound = RoomsFound.NO_ROOMS

    private val _availableRooms = MutableStateFlow<List<Room>>(emptyList())
    val availableRooms: StateFlow<List<Room>> get() = _availableRooms.asStateFlow()

    private val _loadingState = MutableStateFlow<State>(State.Loading)
    val loadingState: StateFlow<State> get() = _loadingState.asStateFlow()

    fun getRoomsAvailableSoon() {
        viewModelScope.launch {
            _loadingState.emit(State.Loading)
            when (val response = repository.getAvailableRooms(SOON)) {
                is RequestResult.Success -> {
                    roomsFound = identifyRooms(response.data)
                    _availableRooms.emit(response.data)
                    _loadingState.emit(State.NotLoading)
                }
                is RequestResult.Error -> {
                    _availableRooms.emit(emptyList())
                    _loadingState.emit(State.Error)
                }
                is RequestResult.Loading -> {
                    _loadingState.emit(State.Loading)
                }
            }
        }
    }

    private fun identifyRooms(rooms: List<Room>): RoomsFound {
        if (rooms.isEmpty()) {
            return RoomsFound.NO_ROOMS
        }
        return if (rooms[0].currentEventEndTime == null && rooms[0].nextEventStartTime == null) {
            RoomsFound.ROOMS_AVAILABLE_SOON
        } else {
            RoomsFound.ROOMS_AVAILABLE_LATER
        }
    }
}

enum class RoomsFound {
    ROOMS_AVAILABLE_SOON,
    ROOMS_AVAILABLE_LATER,
    NO_ROOMS
}