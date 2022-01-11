package com.meetingroom.andersen.feature_landing.data

import com.meetingroom.andersen.feature_landing.domain.entity.ChangedEventDTO
import com.meetingroom.andersen.feature_landing.domain.entity.HistoryEventData
import com.meetingroom.andersen.feature_landing.domain.entity.StatusRoomsDTO
import com.meetingroom.andersen.feature_landing.domain.entity.UpcomingEventData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface RoomsEventApi {
    @GET("api/me/events?type=upcoming")
    suspend fun getUpcomingEventData(): Response<List<UpcomingEventData>>

    @GET("api/me/events?type=history")
    suspend fun getHistoryEvents(): Response<List<HistoryEventData>>

    @PATCH("api/events")
    suspend fun putChangedEvent(@Body event: ChangedEventDTO)

    @DELETE("api/events/{eventId}")
    suspend fun deleteUpcomingEvent(@Path("eventId") eventId: Long)

    suspend fun getRoomPickerNewEventData(startDateTime: String, endDateTime: String): Response<Array<StatusRoomsDTO>>
}