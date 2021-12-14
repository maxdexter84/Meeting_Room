package com.meetingroom.andersen.feature_landing.presentation.modify_upcoming_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_module.event_time_validation.TimeValidationDialogManager
import com.meeringroom.ui.event_dialogs.dialog_room_picker.model.RoomPickerNewEventData
import com.meetingroom.andersen.feature_landing.data.RoomsApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ModifyUpcomingEventViewModel @Inject constructor (
    roomsApi: RoomsApi,
    private val dialogManager: TimeValidationDialogManager
): ViewModel() {

    val effectLiveData = dialogManager.effect
    val stateLiveData = dialogManager.state

    private val _roomPickerArray = MutableStateFlow<Array<RoomPickerNewEventData>>(emptyArray())
    val roomPickerArray: StateFlow<Array<RoomPickerNewEventData>> get() = _roomPickerArray.asStateFlow()

    init {
        _roomPickerArray.value = roomsApi.getRoomPickerNewEventData()
    }

    fun setEvent(event : TimeValidationDialogManager.ValidationEvent) {
        viewModelScope.launch { dialogManager.handleEvent(event) }
    }
}
