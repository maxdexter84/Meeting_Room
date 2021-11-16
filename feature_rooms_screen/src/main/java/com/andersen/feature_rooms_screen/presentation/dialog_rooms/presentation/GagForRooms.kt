package com.andersen.feature_rooms_screen.presentation.dialog_rooms.presentation

import com.andersen.feature_rooms_screen.domain.entity.Room

class GagForRooms {
    fun generate(): List<Room> {
        val list = arrayListOf<Room>()
        for (i in 0..5) {
            val title = when (i) {
                0 -> "Amsterdam"
                1 -> "Valencia"
                2 -> "Berlin"
                3 -> "London"
                4 -> "Paris"
                else -> "Rome"
            }

            val floor = when(i) {
                0,1,2,3,4 -> 1
                else -> 2
            }
            list += Room(20, floor, 1234, "office", title, 1234)
        }
        return list
    }
}