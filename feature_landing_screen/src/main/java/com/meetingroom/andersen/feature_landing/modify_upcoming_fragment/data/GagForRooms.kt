package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.data

import com.meeringroom.ui.event_dialogs.dialog_room_picker.model.RoomPickerNewEventData
import javax.inject.Inject

class GagForRooms @Inject constructor() {
    private val arraySize = 8

    fun generate(): Array<RoomPickerNewEventData> {
        val array = Array(arraySize) { i ->
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