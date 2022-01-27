package com.andersen.feature_rooms_screen.data

import android.graphics.Color
import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.domain.entity.RoomEvent
import com.andersen.feature_rooms_screen.presentation.rooms_event_grid.RoomsEventGridFragment
import javax.inject.Inject
import kotlin.collections.ArrayList

class GagForRoomsScreen @Inject constructor() : RoomsApi {


    override fun getFreeRooms(): List<Room> {
        val list = ArrayList<Room>()
            list.addAll(listOf(
                Room(6, 2, 1, "Odessa", "Blue", Color.BLUE.toString(), false, false),
                Room(4, 3, 2, "Kyiv", "Green", Color.GREEN.toString(), true, false),
                Room(2, 4, 5, "Kazan", "Magenta", Color.MAGENTA.toString(), true, true),
                Room(2, 11, 6, "Vladivostok", "Red", Color.RED.toString(), true, true),
                Room(2, 21, 7, "Minsk", "Yellow", Color.YELLOW.toString(), true, true),
                Room(2, 21, 9, "Gomel", "Green", Color.GREEN.toString(), true, true))
            )
        return list
    }
}