package com.andersen.feature_rooms_screen.data

import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.domain.entity.remote.RoomsEventDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RoomsScreenApi {
    @GET("api/rooms")
    suspend fun getRoomsApi(@Query("officeId") officeId: Int): Response<List<Room>>

    @GET("api/rooms")
    suspend fun getAllRoomsOnTheFloorApi(@Query("floor") floor: String,
                                         @Query("officeId") officeId: Int ): Response<List<Room>>

    @GET("api/rooms")
    suspend fun getOneRoomApi(@Query("title") title: String): Response<Room>

    @GET("api/rooms/events")
    suspend fun getRoomsEventsApi(@Query("day") title: String): Response<List<RoomsEventDTO>>
}