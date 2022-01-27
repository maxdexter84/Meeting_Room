package com.andersen.feature_rooms_screen.domain.entity


import com.andersen.feature_rooms_screen.domain.entity.remote.RoomsEventDTO
import com.example.core_network.RequestResult

interface InRoomsScreenRepository {

    suspend fun getRoomsApi(officeId: Int): RequestResult<List<Room>>

    suspend fun getAllRoomsOnTheFloorApi(floor: String, officeId: Int): RequestResult<List<Room>>

    suspend fun getOneRoomApi(title: String): RequestResult<Room>

    suspend fun getRoomsEventsApi(date: String): RequestResult<List<RoomsEventDTO>>
}