package com.andersen.feature_rooms_screen.presentation.dialog_rooms.presentation

import com.andersen.feature_rooms_screen.presentation.dialog_rooms.model.GagRoomData

class GagForRooms {
    fun generate(): List<GagRoomData> {
        val list = arrayListOf<GagRoomData>()
        for (i in 0..6) {
            val data = when (i) {
                0 -> "Amsterdam"
                1 -> "Valencia"
                2 -> "Berlin"
                3 -> "London"
                4 -> "Paris"
                5 -> "All rooms on 2nd floor"
                else -> "Rome"
            }
            list += GagRoomData(data)
        }
        return list
    }
}