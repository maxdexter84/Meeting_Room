package com.andersen.feature_rooms_screen.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andersen.feature_rooms_screen.data.RoomsApi
import com.andersen.feature_rooms_screen.domain.entity.InRoomsScreenRepository
import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.domain.entity.RoomEvent
import com.andersen.feature_rooms_screen.domain.entity.mappers.toEventList
import com.andersen.feature_rooms_screen.domain.entity.remote.ActivateRoomEventDto
import com.andersen.feature_rooms_screen.domain.entity.remote.DateDTO
import com.andersen.feature_rooms_screen.domain.entity.remote.RoomEventCreateDto
import com.andersen.feature_rooms_screen.domain.entity.remote.RoomStatusDTO
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

    private val _mutableEventNow = MutableStateFlow(true)
    val mutableEventNow: StateFlow<Boolean> get() = _mutableEventNow.asStateFlow()

    private val _mutableSaveBookingStatus = MutableStateFlow<Boolean?>(null)
    val mutableSaveBookingStaus : StateFlow<Boolean?> get() = _mutableSaveBookingStatus

    private val _mutableRoomListByFloor = MutableStateFlow<List<Room>>(emptyList())
    val mutableRoomListByFloor: StateFlow<List<Room>> get() = _mutableRoomListByFloor.asStateFlow()

    private var idEvent: Long = 0

    private val _mutableRoomStatusList = MutableStateFlow<List<RoomStatusDTO>>(emptyList())
    val mutableRoomStatusList: StateFlow<List<RoomStatusDTO>> get() = _mutableRoomStatusList.asStateFlow()

    val effectLiveData = dialogManager.effect
    val stateLiveData = dialogManager.state

    lateinit var currentList: List<Room>

    var userRole: String? = null

    fun createEvent(event: RoomEventCreateDto) {
        viewModelScope.launch {
            when (val res = roomsScreenRepository.createEvent(event)) {
                is RequestResult.Success -> {
                    idEvent = res.data.id
                }
                is RequestResult.Error -> {
                    Log.d(TAG_CHECK, MESSAGE_ERROR)
                }
                else -> Unit
            }
        }
    }

    fun activateEvent(
        description: String,
        roomId: Long,
        startDateTime: String,
        endDateTime: String,
        title: String
    ) {
        viewModelScope.launch {
            try {
                _mutableLoadingState.emit(State.Loading)
                when (val res = roomsScreenRepository.activateEvent(
                    ActivateRoomEventDto(
                        description = description,
                        roomId = roomId,
                        startDateTime = startDateTime,
                        endDateTime = endDateTime,
                        title = title,
                        id = idEvent
                    )
                )) {
                    is RequestResult.Error -> {
                        if (res.code == NULL_POINTER_EXCEPTION_CODE) {
                            _mutableSaveBookingStatus.emit(true)
                        } else {
                            _mutableSaveBookingStatus.emit(false)
                        }
                    }
                    else -> Unit
                }
                _mutableLoadingState.emit(State.NotLoading)
            } catch (e: Exception) {
                _mutableLoadingState.emit(State.Error)
                _mutableSaveBookingStatus.emit(false)
            }
        }
    }

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

    fun getFreeRoomsList(startDate: String, endDate: String, chosenRoomId : Long) {
        val date = DateDTO(startDate, endDate)
        viewModelScope.launch {
            try {
                _mutableLoadingState.emit(State.Loading)
                delay(DELAY_DOWNLOAD)
                when (val response = roomsScreenRepository.getFreeRooms(date)) {
                    is RequestResult.Success -> {
                        _mutableRoomStatusList.emit(response.data)
                        checkChoosenRoom(chosenRoomId, response.data)
                    }
                    is RequestResult.Error -> {
                        _mutableRoomStatusList.emit(emptyList())
                    }
                }
                _mutableLoadingState.emit(State.NotLoading)
            } catch (exception: Exception) {
                _mutableLoadingState.emit(State.Error)
            }
        }
    }

    fun checkChoosenRoom( chosenRoomId: Long, roomStatusList: List<RoomStatusDTO>) {
        var isNotEventNow = true
        roomStatusList.forEach {
            if (it.id==chosenRoomId && it.status == STATUS_OCCUPIED && !(it.eventIdsList.contains(idEvent))) isNotEventNow = false
        }
        viewModelScope.launch { _mutableEventNow.emit(isNotEventNow) }
    }

    private fun getRoomPickerData() {
        viewModelScope.launch {
            mutableRoomStatusList.collectLatest { it ->
                val roomsList = Array(it.size) { index ->
                    RoomPickerNewEventData(
                        it[index].id,
                        it[index].title,
                        ROOM_IS_SELECTED,
                        it[index].status == STATUS_FREE || (it[index].status == STATUS_OCCUPIED && it[index].eventIdsList.contains(idEvent))
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

    fun getUserRole() {
        viewModelScope.launch {
            userRole = userDataPrefHelper.getUserRole()
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
        const val NULL_POINTER_EXCEPTION_CODE = 115
        const val DELAY_DOWNLOAD = 500L
        private const val ROOM_IS_SELECTED = false
        const val STATUS_FREE = "FREE"
        const val STATUS_OCCUPIED = "OCCUPIED"
        const val TAG_CHECK = "proverkaError"
        const val MESSAGE_BAD = "bad"
        const val MESSAGE_ERROR = "error"
    }
}