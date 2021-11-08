package com.andersen.feature_rooms_screen.presentation.rooms_event_grid

import android.graphics.Color
import com.andersen.feature_rooms_screen.domain.entity.Room

class GagForRooms {

    val rooms = generateRooms(SIZE_ROOM)

    private fun generateRooms(size: Int): List<Room> {
        val list = ArrayList<Room>()
        for (i in 0 until size) {
            list += when (i % 9) {

                0 -> Room(5, 8, 0, "Dnipro", "Gray", Color.GRAY)
                1 -> Room(6, 8, 1, "Odessa", "Blue", Color.BLUE)
                2 -> Room(4, 8, 2, "Kyiv", "Green", Color.GREEN)
                3 -> Room(3, 8, 3, "Moscow", "Black", Color.BLACK)
                4 -> Room(2, 8, 4, "Piter", "Drkgray", Color.DKGRAY)
                5 -> Room(2, 8, 5, "Kazan", "Magenta", Color.MAGENTA)
                6 -> Room(2, 8, 6, "Vladivostok", "Red", Color.RED)
                7 -> Room(2, 8, 7, "Minsk", "Yellow", Color.YELLOW)
                else -> Room(2, 8, 9, "Gomel", "Green", Color.GREEN)
            }
        }
        return list
    }

    companion object{
       const val SIZE_ROOM = 8
    }
}