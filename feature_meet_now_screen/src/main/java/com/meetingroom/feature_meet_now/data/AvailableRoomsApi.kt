package com.meetingroom.feature_meet_now.data

import com.andersen.feature_rooms_screen.domain.entity.remote.ActivateRoomEventDto
import com.andersen.feature_rooms_screen.domain.entity.remote.RoomEventCreateDto
import com.andersen.feature_rooms_screen.domain.entity.remote.RoomEventResponseDto
import com.meetingroom.feature_meet_now.domain.entity.Room
import retrofit2.Response
import retrofit2.http.*

interface AvailableRoomsApi {

    @GET("api/rooms/active")
    suspend fun getAvailableRooms(
        @Query("time") time: String
    ): Response<List<Room>>

    @POST("/api/events")
    suspend fun createEvent(@Body event: RoomEventCreateDto): Response<RoomEventResponseDto>

    @PATCH("/api/events")
    suspend fun activateEvent(@Body event: ActivateRoomEventDto): Response<Unit>
}