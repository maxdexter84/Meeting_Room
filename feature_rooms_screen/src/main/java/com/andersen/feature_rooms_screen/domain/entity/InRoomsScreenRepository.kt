package com.andersen.feature_rooms_screen.domain.entity


import com.andersen.feature_rooms_screen.domain.entity.remote.*
import com.example.core_network.RequestResult

interface InRoomsScreenRepository {

    suspend fun getRoomsApi(officeId: Int): RequestResult<List<Room>>

    suspend fun getAllRoomsOnTheFloorApi(floor: String, officeId: Int): RequestResult<List<Room>>

    suspend fun getOneRoomApi(title: String): RequestResult<Room>

    suspend fun getRoomsEventsApi(date: String): RequestResult<List<RoomsEventDTO>>

    suspend fun createEvent(event: RoomEventCreateDto): RequestResult<RoomEventResponseDto>

    suspend fun getFreeRooms(date: DateDTO): RequestResult<List<RoomStatusDTO>>

    suspend fun activateEvent(event: ActivateRoomEventDto): RequestResult<Unit>
}