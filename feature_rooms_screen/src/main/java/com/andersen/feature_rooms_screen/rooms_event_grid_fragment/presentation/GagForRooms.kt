package com.andersen.feature_rooms_screen.rooms_event_grid_fragment.presentation

import com.andersen.feature_rooms_screen.rooms_event_grid_fragment.model.Room

class GagForRooms {

    val rooms = generateRooms(5)

    private fun generateRooms(size: Int): List<Room> {
        val list = ArrayList<Room>()
        for (i in 0 until size) {
            list += when (i % 9) {
                0 -> Room(5, 8, 0, "Piter", "yellow")
                1 -> Room(6, 8, 1, "Piter", "orange")
                2 -> Room(4, 8, 2, "Piter", "gray")
                3 -> Room(3, 8, 3, "Piter", "red")
                4 -> Room(2, 8, 4, "Piter", "blue")
                5 -> Room(2, 8, 5, "Piter", "black")
                6 -> Room(2, 8, 6, "Piter", "purple")
                7 -> Room(2, 8, 7, "Piter", "green")
                8 -> Room(2, 8, 8, "Piter", "magenta")
                else -> Room(2, 8, 9, "Piter", "white")
            }
        }
        return list
    }

    fun getRoomsAmount() = rooms.size
}