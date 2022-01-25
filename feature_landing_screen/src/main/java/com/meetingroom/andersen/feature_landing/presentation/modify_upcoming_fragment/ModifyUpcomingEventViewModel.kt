package com.meetingroom.andersen.feature_landing.presentation.modify_upcoming_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_module.event_time_validation.TimeValidationDialogManager
import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper
import com.example.core_module.state.State
import com.example.core_network.RequestResult
import com.meeringroom.ui.event_dialogs.dialog_room_picker.model.RoomPickerNewEventData
import com.meetingroom.andersen.feature_landing.domain.entity.ChangedEventDTO
import com.meetingroom.andersen.feature_landing.domain.entity.IRoomsEventRepository
import com.meetingroom.andersen.feature_landing.domain.entity.StatusRoomsDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ModifyUpcomingEventViewModel @Inject constructor(
    private val dialogManager: TimeValidationDialogManager,
    private val roomsEventRepository: IRoomsEventRepository
) : ViewModel() {

    @Inject
    lateinit var userDataPrefHelper: UserDataPrefHelper

    val effectLiveData = dialogManager.effect
    val stateLiveData = dialogManager.state

    private val _roomPickerArray = MutableStateFlow<Array<RoomPickerNewEventData>>(emptyArray())
    val roomPickerArray: StateFlow<Array<RoomPickerNewEventData>> get() = _roomPickerArray.asStateFlow()

    private val _mutableLoadingState = MutableStateFlow<State>(State.Loading)
    val mutableState: StateFlow<State> get() = _mutableLoadingState.asStateFlow()

    suspend fun deleteEvent(eventId: Long) {
        viewModelScope.launch {
            try {
                _mutableLoadingState.emit(State.Loading)
                roomsEventRepository.deleteUpcomingEvent(eventId)
                val role = userDataPrefHelper.getUserRole().toString()
                if(role == ROLE_ADMIN){
                    roomsEventRepository.deleteUpcomingEventForAdmin(eventId)
                } else{
                    roomsEventRepository.deleteUpcomingEvent(eventId)
                }
                _mutableLoadingState.emit(State.NotLoading)
            } catch (exception: Exception) {
                _mutableLoadingState.emit(State.Error)
            }
        }
    }

    fun putChangedEvent(event: ChangedEventDTO) {
        viewModelScope.launch {
            try {
                _mutableLoadingState.emit(State.Loading)
                val role = userDataPrefHelper.getUserRole().toString()
                if(role == ROLE_ADMIN){
                    roomsEventRepository.putChangedEventForAdmin(event)
                } else{
                    roomsEventRepository.putChangedEvent(event)
                }
                _mutableLoadingState.emit(State.NotLoading)
            } catch (exception: Exception) {
                _mutableLoadingState.emit(State.Error)
            }
        }
    }

    fun getRoomsEvent(startDateTime: String, endDateTime: String) {
        viewModelScope.launch {
            try {
                val response =
                    roomsEventRepository.getRoomPickerNewEventData(startDateTime, endDateTime)
                checkResponse(response)
            } catch (exception: Exception) {
                _roomPickerArray.value = emptyArray()
                _mutableLoadingState.emit(State.Error)
            }
        }
    }

    fun setEvent(event: TimeValidationDialogManager.ValidationEvent) {
        viewModelScope.launch { dialogManager.handleEvent(event) }
    }

    private fun checkResponse(response: RequestResult<Array<StatusRoomsDTO>>) {
        when (response) {
            is RequestResult.Success -> {
                val listStatusRoomsDTO = response.data.toList()
                val listRoomPickerNewEventData = mutableListOf<RoomPickerNewEventData>()
                listStatusRoomsDTO.forEach {
                    listRoomPickerNewEventData.add(
                        RoomPickerNewEventData(
                            it.id,
                            it.title,
                            it.isSelected,
                            it.isEnabled
                        )
                    )
                }
                _roomPickerArray.value = listRoomPickerNewEventData.toTypedArray()
            }
            else -> _roomPickerArray.value = emptyArray()
        }
    }

    companion object {
        private const val ROLE_ADMIN = "ROLE_ADMIN"
    }
}
