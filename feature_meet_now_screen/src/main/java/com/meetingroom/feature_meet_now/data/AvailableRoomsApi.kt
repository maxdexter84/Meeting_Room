package com.meetingroom.feature_meet_now.data

import com.meetingroom.feature_meet_now.domain.entity.Room
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AvailableRoomsApi {

    @GET("api/rooms/active")
    suspend fun getAvailableRooms(
        @Query("time") time: String
    ): Response<List<Room>>
}