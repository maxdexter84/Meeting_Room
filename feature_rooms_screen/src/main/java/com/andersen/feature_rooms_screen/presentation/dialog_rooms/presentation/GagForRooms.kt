package com.andersen.feature_rooms_screen.presentation.dialog_rooms.presentation

import com.andersen.feature_rooms_screen.presentation.dialog_rooms.model.GagRoomData

class GagForRooms {
    fun generate(): List<GagRoomData> {
        val list = arrayListOf<GagRoomData>()
        for (i in 0..5) {
            val data = when (i) {
                0 -> "Amsterdam"
                1 -> "Valencia"
                2 -> "Berlin"
                3 -> "London"
                4 -> "Paris"
                else -> "Rome"
            }

            val isFloor = when(i) {
                0,1,2,3,4 -> 1
                else -> 2
            }
            list += GagRoomData(data, isFloor)
        }
        return list
    }
}