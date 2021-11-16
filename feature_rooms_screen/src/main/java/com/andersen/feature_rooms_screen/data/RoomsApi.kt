package com.andersen.feature_rooms_screen.data

import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.domain.entity.RoomEvent
import com.andersen.feature_rooms_screen.presentation.dialog_rooms.presentation.GagForRooms

interface RoomsApi {

    fun getRoomEvents(): List<RoomEvent>

    fun getRooms(): List<Room>

    fun getGagRoomsForDialog(): List<Room> = GagForRooms().generate()
}