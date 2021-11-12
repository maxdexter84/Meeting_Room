package com.andersen.feature_rooms_screen.presentation.rooms_event_grid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andersen.feature_rooms_screen.data.RoomsApi
import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.domain.entity.RoomEvent
import com.example.core_module.state.State
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomsEventViewModel @Inject constructor(
    private val roomsApi: RoomsApi,
    val roomsAdapter: RoomsAdapter,
    val mainEventAdapter: MainEventAdapter,
) : ViewModel() {

    private val _mutableRoomEventList = MutableStateFlow<List<RoomEvent>>(emptyList())
    val mutableRoomEventList: StateFlow<List<RoomEvent>> get() = _mutableRoomEventList.asStateFlow()

    private val _mutableRoomList = MutableStateFlow<List<Room>>(emptyList())
    val mutableRoomList: StateFlow<List<Room>> get() = _mutableRoomList.asStateFlow()

    private val _mutableLoadingState = MutableStateFlow<State>(State.Loading)
    val mutableState: StateFlow<State> get() = _mutableLoadingState.asStateFlow()

    private fun getRoomList() {
        viewModelScope.launch {
            try {
                _mutableLoadingState.emit(State.Loading)
                delay(DELAY_DOWNLOAD)
                _mutableRoomList.emit(roomsApi.getRooms())
                _mutableLoadingState.emit(State.NotLoading)
            } catch (exception: Exception) {
                _mutableLoadingState.emit(State.Error)
            }
        }
    }

    fun getEventList(date: CalendarDay?) {
        viewModelScope.launch {
            try {
                _mutableLoadingState.emit(State.Loading)
                delay(DELAY_DOWNLOAD)
                _mutableRoomEventList.emit(roomsApi.getRoomEvents())
                _mutableLoadingState.emit(State.NotLoading)
            } catch (exception: Exception) {
                _mutableLoadingState.emit(State.Error)
            }
        }
    }

    init {
        getRoomList()
    }

    companion object{
        const val DELAY_DOWNLOAD = 500L
    }
}