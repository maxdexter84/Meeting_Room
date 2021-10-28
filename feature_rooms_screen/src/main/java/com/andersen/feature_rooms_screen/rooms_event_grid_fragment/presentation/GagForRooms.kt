package com.andersen.feature_rooms_screen.rooms_event_grid_fragment.presentation

import com.andersen.feature_rooms_screen.rooms_event_grid_fragment.model.Room
import com.meetingroom.andersen.feature_rooms_screen.R

class GagForRooms {

    val rooms = generateRooms(5)

    private fun generateRooms(size: Int): List<Room> {
        val list = ArrayList<Room>()
        for (i in 0 until size) {
            list += when (i % 9) {
                0 -> Room(5, 8, 0, "Piter", "gray", R.color.design_default_color_primary)
                1 -> Room(6, 8, 1, "Piter", "orange", R.color.purple_light)
                2 -> Room(4, 8, 2, "Piter", "gray", R.color.purple_dark)
                3 -> Room(3, 8, 3, "Piter", "purple", R.color.purple)
                4 -> Room(2, 8, 4, "Piter", "green", R.color.teal_dark)
                5 -> Room(2, 8, 5, "Piter", "yellow", R.color.yellow_for_selected_item)
                6 -> Room(2, 8, 6, "Piter", "purple", R.color.error_color_for_line_under_edittext)
                7 -> Room(2, 8, 7, "Piter", "red", R.color.error_color_for_line_under_edittext)
                else -> Room(2, 8, 9, "Piter", "white", R.color.design_default_color_primary)
            }
        }
        return list
    }

    fun getRoomsAmount() = rooms.size
}