package com.meetingroom.andersen.feature_landing.presentation.modify_upcoming_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_module.event_time_validation.TimeValidationDialogManager
import com.meeringroom.ui.event_dialogs.dialog_room_picker.model.RoomPickerNewEventData
import com.meetingroom.andersen.feature_landing.data.RoomsEventApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ModifyUpcomingEventViewModel @Inject constructor (
    private val dialogManager: TimeValidationDialogManager
): ViewModel() {

    val effectLiveData = dialogManager.effect
    val stateLiveData = dialogManager.state

    private val _roomPickerArray = MutableStateFlow<Array<RoomPickerNewEventData>>(emptyArray())
    val roomPickerArray: StateFlow<Array<RoomPickerNewEventData>> get() = _roomPickerArray.asStateFlow()

    init {
        _roomPickerArray.value = getRoomPickerNewEventData()
    }

    fun setEvent(event : TimeValidationDialogManager.ValidationEvent) {
        viewModelScope.launch { dialogManager.handleEvent(event) }
    }

    //GAG
    fun getRoomPickerNewEventData(): Array<RoomPickerNewEventData> {
        val array = Array(6) { i ->
            val room = when (i) {
                0 -> "Gray"
                1 -> "Blue"
                2 -> "Green"
                3 -> "Black"
                4 -> "Drkgray"
                5 -> "Magenta"
                6 -> "Red"
                7 -> "Yellow"
                else -> "Green"
            }

            val isSelected = false

            val isEnabled = when(i) {
                3,4 -> false
                else -> true
            }
            RoomPickerNewEventData(room, isSelected, isEnabled)
        }
        return array.sortedByDescending { room -> room.isEnabled }.toTypedArray()
    }
}
