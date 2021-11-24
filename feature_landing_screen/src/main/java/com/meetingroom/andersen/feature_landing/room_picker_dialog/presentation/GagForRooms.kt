package com.meetingroom.andersen.feature_landing.room_picker_dialog.presentation

import com.meetingroom.andersen.feature_landing.room_picker_dialog.model.GagRoomData

class GagForRooms {
    fun generate(): List<GagRoomData> {
        val list = arrayListOf<GagRoomData>()
        for (i in 0..6) {
            val data = when (i) {
                0 -> "Red"
                1 -> "Orange"
                2 -> "Yellow"
                3 -> "Green"
                4 -> "Blue"
                5 -> "Gray"
                else -> "Purple"
            }

            val isBusy = when(i) {
                5,6 -> true
                else -> false
            }
            list += GagRoomData(data, isBusy)
        }
        return list
    }
}