package com.andersen.feature_rooms_screen.data

import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.domain.entity.RoomEvent

interface RoomsApi {

    fun getRoomEvents(): List<RoomEvent>

    fun getRooms(): List<Room>
}