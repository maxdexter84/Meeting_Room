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
private const val SIX_MIN = 6
private const val HALF_HOUR = 30
private const val MAX_BOOKING_TIME = 90

class RoomsAvailableSoonViewModel @Inject constructor(
    private val repository: AvailableRoomsRepository
) : ViewModel() {
    var viewState = ViewState.NO_ROOMS_FOUND

    private val _availableRooms = MutableStateFlow<List<Room>>(emptyList())
    val availableRooms: StateFlow<List<Room>> get() = _availableRooms.asStateFlow()

    private val _loadingState = MutableStateFlow<State>(State.Loading)
    val loadingState: StateFlow<State> get() = _loadingState.asStateFlow()

    fun getRoomsAvailableSoon() {
        viewModelScope.launch {
            _loadingState.emit(State.Loading)
            when (val response = repository.getAvailableRooms(SOON)) {
                is RequestResult.Success -> {
                    viewState = getViewState(response.data)
                    val rooms = when(viewState) {
                        ViewState.ROOMS_AVAILABLE_SOON_FOUND -> filterRoomsAvailableSoon(validateRooms(response.data))
                        ViewState.ROOMS_AVAILABLE_LATER_FOUND -> filterRoomsAvailableLater(response.data)
                        ViewState.NO_ROOMS_FOUND -> emptyList()
                    }
                    _availableRooms.emit(rooms)
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

    private fun getViewState(rooms: List<Room>): ViewState {
        if (rooms.any { room ->
                room.availableIn in SIX_MIN..HALF_HOUR
            }) return ViewState.ROOMS_AVAILABLE_SOON_FOUND
        if (rooms.any { room ->
                room.availableIn > HALF_HOUR
            }) return ViewState.ROOMS_AVAILABLE_LATER_FOUND
        else {
            return ViewState.NO_ROOMS_FOUND
        }
    }

    private fun filterRoomsAvailableSoon(rooms: List<Room>): List<Room> {
        return rooms.filter { room ->
            room.availableIn in SIX_MIN..HALF_HOUR
        }
    }

    private fun filterRoomsAvailableLater(rooms: List<Room>): List<Room> {
        return rooms.filter { room ->
            room.availableIn > HALF_HOUR
        }
    }

    private fun validateRooms(rooms: List<Room>): List<Room> {
        for (room in rooms) {
            room.apply {
                timeUntilNextEvent = timeUntilNextEvent?.coerceAtMost(MAX_BOOKING_TIME)
                    ?: MAX_BOOKING_TIME
            }
        }
        return rooms
    }
}

enum class ViewState {
    ROOMS_AVAILABLE_SOON_FOUND,
    ROOMS_AVAILABLE_LATER_FOUND,
    NO_ROOMS_FOUND
}