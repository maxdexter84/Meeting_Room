package com.meetingroom.andersen.feature_landing.room_picker_dialog.presentation

import com.meetingroom.andersen.feature_landing.room_picker_dialog.model.GagRoomData

class GagForRooms {
    fun generate(): List<GagRoomData> {
        val list = arrayListOf<GagRoomData>()
        for (i in 0..6) {
            val data = when (i) {
                0 -> "Amsterdam"
                1 -> "Berlin"
                2 -> "Valencia"
                3 -> "Rome"
                4 -> "Moscow"
                5 -> "Paris"
                else -> "London"
            }
            list += GagRoomData(data)
        }
        return list
    }
}