package com.andersen.feature_rooms_screen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andersen.feature_rooms_screen.data.RoomsApi
import com.andersen.feature_rooms_screen.domain.entity.InRoomsScreenRepository
import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.domain.entity.RoomEvent
import com.andersen.feature_rooms_screen.domain.entity.mappers.toEventList
import com.andersen.feature_rooms_screen.presentation.utils.toApiDate
import com.example.core_module.event_time_validation.TimeValidationDialogManager
import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper
import com.example.core_module.state.State
import com.example.core_network.RequestResult
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
    private val dialogManager: TimeValidationDialogManager,
    private val roomsScreenRepository: InRoomsScreenRepository,
) : ViewModel() {

    @Inject
    lateinit var userDataPrefHelper: UserDataPrefHelper

    private val _mutableRoomEventList = MutableStateFlow<List<RoomEvent>>(emptyList())
    val mutableRoomEventList: StateFlow<List<RoomEvent>> get() = _mutableRoomEventList.asStateFlow()

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

    lateinit var currentList: List<Room>

    fun setEvent(event: TimeValidationDialogManager.ValidationEvent) {
        viewModelScope.launch { dialogManager.handleEvent(event) }
    }

    private fun getRoomList() {
        viewModelScope.launch {
            try {
                _mutableLoadingState.emit(State.Loading)
                delay(DELAY_DOWNLOAD)
                val response = userDataPrefHelper.getOfficeIdOfUserLocation()?.let {
                    roomsScreenRepository.getRoomsApi(it)
                }
                when (response) {
                    is RequestResult.Success -> {
                        _mutableRoomList.emit(response.data)
                        currentList = response.data
                    }
                    is RequestResult.Error -> {
                        _mutableRoomList.emit(emptyList())
                    }
                    else -> _mutableRoomList.emit(emptyList())
                }
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
                        it[index].id,
                        it[index].title,
                        ROOM_IS_SELECTED,
                        it[index] in freeRooms
                    )
                }
                _roomPickerList.emit(roomsList.sortedByDescending { room -> room.isEnabled }
                    .toTypedArray())
            }
        }
    }

    fun getEventList(date: CalendarDay?) {
        viewModelScope.launch {
            try {
                _mutableLoadingState.emit(State.Loading)
                delay(DELAY_DOWNLOAD)
                val response = date?.let { roomsScreenRepository.getRoomsEventsApi(it.toApiDate()) }
                when (response) {
                    is RequestResult.Success -> {
                        val eventDto = response.data.filterNot { it.events.isEmpty() }
                        if (eventDto.isEmpty()) {
                            _mutableRoomEventList.emit(emptyList())
                        } else {
                            _mutableRoomEventList.emit(eventDto.toEventList())
                        }
                    }
                    is RequestResult.Error -> {
                        _mutableRoomEventList.emit(emptyList())
                    }
                }

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
                val response = roomsScreenRepository.getOneRoomApi(roomTitle)
                when (response) {
                    is RequestResult.Success -> {
                        _mutableRoom.emit(response.data)
                    }
                    is RequestResult.Error -> {
                        _mutableLoadingState.emit(State.Error)
                    }
                }
                _mutableLoadingState.emit(State.NotLoading)
            } catch (exception: Exception) {
                _mutableLoadingState.emit(State.Error)
            }
        }
    }

    fun getRoomsOnTheFloor(floor: String) {
        viewModelScope.launch {
            try {
                _mutableLoadingState.emit(State.Loading)
                delay(DELAY_DOWNLOAD)
                val response = userDataPrefHelper.getOfficeIdOfUserLocation()?.let {
                    roomsScreenRepository.getAllRoomsOnTheFloorApi(floor, it)
                }
                when (response) {
                    is RequestResult.Success -> {
                        _mutableRoomListByFloor.emit(response.data)
                    }
                    is RequestResult.Error -> {
                        _mutableRoomListByFloor.emit(emptyList())
                    }
                    else -> _mutableRoomListByFloor.emit(emptyList())
                }
                _mutableLoadingState.emit(State.NotLoading)
            } catch (exception: Exception) {
                _mutableLoadingState.emit(State.Error)
            }
        }
    }

    init {
        getRoomList()
        getRoomPickerData()
    }

    companion object {
        const val DELAY_DOWNLOAD = 500L
        private const val ROOM_IS_SELECTED = false
    }
}