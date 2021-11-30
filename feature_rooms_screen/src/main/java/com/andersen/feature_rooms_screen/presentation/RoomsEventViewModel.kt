package com.andersen.feature_rooms_screen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andersen.feature_rooms_screen.data.RoomsApi
import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.domain.entity.RoomEvent
import com.andersen.feature_rooms_screen.presentation.rooms_event_grid.multiple_room_grid.MainEventAdapter
import com.andersen.feature_rooms_screen.presentation.rooms_event_grid.multiple_room_grid.RoomsAdapter
import com.example.core_module.event_time_validation.TimeValidationDialogManager
import com.andersen.feature_rooms_screen.presentation.rooms_event_grid.single_room_event.SingleRoomEventAdapter
import com.example.core_module.state.State
import com.meeringroom.ui.event_dialogs.dialog_room_picker.model.RoomPickerNewEventData
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomsEventViewModel @Inject constructor(
    private val roomsApi: RoomsApi,
    val roomsAdapter: RoomsAdapter,
    val mainEventAdapter: MainEventAdapter,
    val singleRoomEventAdapter: SingleRoomEventAdapter,
    private val dialogManager: TimeValidationDialogManager
) : ViewModel() {

    private val _mutableRoomEventList = MutableStateFlow<List<RoomEvent>>(emptyList())
    val mutableRoomEventList: StateFlow<List<RoomEvent>> get() = _mutableRoomEventList.asStateFlow()

    private val _mutableRoomEventListByRoom = MutableStateFlow<List<RoomEvent>>(emptyList())
    val mutableRoomEventListByRoom: StateFlow<List<RoomEvent>> get() = _mutableRoomEventListByRoom.asStateFlow()

    private val _mutableRoomList = MutableStateFlow<List<Room>>(emptyList())
    val mutableRoomList: StateFlow<List<Room>> get() = _mutableRoomList.asStateFlow()

    private val _roomPickerList = MutableStateFlow<Array<RoomPickerNewEventData>>(emptyArray())
    val roomPickerList: StateFlow<Array<RoomPickerNewEventData>> get() = _roomPickerList.asStateFlow()

    private val _mutableLoadingState = MutableStateFlow<State>(State.Loading)
    val mutableState: StateFlow<State> get() = _mutableLoadingState.asStateFlow()

    private val _mutableRoom = MutableStateFlow<Room?>(null)
    val room: StateFlow<Room?> get() = _mutableRoom.asStateFlow()

    private val _mutableRoomListByFloor = MutableStateFlow<List<Room>>(emptyList())
    val mutableRoomListByFloor: StateFlow<List<Room>> get() = _mutableRoomListByFloor.asStateFlow()

    val effectLiveData = dialogManager.effect
    val stateLiveData = dialogManager.state

    fun setEvent(event : TimeValidationDialogManager.ValidationEvent) {
        viewModelScope.launch { dialogManager.handleEvent(event) }
    }

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

    fun getFreeRoomsList() = roomsApi.getFreeRooms()

    private fun getRoomPickerData() {
        viewModelScope.launch {
            mutableRoomList.collectLatest { it ->
                val freeRooms = getFreeRoomsList()
                val roomsList = Array(it.size) { index ->
                    RoomPickerNewEventData(
                        it[index].title,
                        ROOM_IS_SELECTED,
                        it[index] in freeRooms
                    )
                }
                _roomPickerList.emit(roomsList.sortedByDescending { room -> room.isEnabled }.toTypedArray())
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

    fun getRoom(roomTitle: String) {
        viewModelScope.launch {
            try {
                _mutableLoadingState.emit(State.Loading)
                delay(DELAY_DOWNLOAD)
                _mutableRoom.emit(roomsApi.getOneRoom(roomTitle))
                _mutableLoadingState.emit(State.NotLoading)
            } catch (exception: Exception) {
                _mutableLoadingState.emit(State.Error)
            }
        }
    }

    fun getRoomsOnTheFloor(floor: Int){
        viewModelScope.launch {
            try {
                _mutableLoadingState.emit(State.Loading)
                delay(DELAY_DOWNLOAD)
                _mutableRoomListByFloor.emit(roomsApi.getAllRoomsOnTheFloor(floor))
                _mutableLoadingState.emit(State.NotLoading)
            } catch (exception: Exception) {
                _mutableLoadingState.emit(State.Error)
            }
        }
    }

    private fun getEventsByRoom(){
        viewModelScope.launch {
            try {
                _mutableLoadingState.emit(State.Loading)
                delay(DELAY_DOWNLOAD)
                _mutableRoomEventListByRoom.emit(roomsApi.getRoomEventsByRoom())
                _mutableLoadingState.emit(State.NotLoading)
            } catch (exception: Exception) {
                _mutableLoadingState.emit(State.Error)
            }
        }
    }

    init {
        getRoomList()
        getEventsByRoom()
        getRoomPickerData()
    }

    companion object{
        const val DELAY_DOWNLOAD = 500L
        private const val ROOM_IS_SELECTED = false
    }
}