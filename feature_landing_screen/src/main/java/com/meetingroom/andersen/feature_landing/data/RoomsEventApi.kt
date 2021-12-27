package com.meetingroom.andersen.feature_landing.data

import com.meetingroom.andersen.feature_landing.domain.entity.HistoryEventData
import com.meetingroom.andersen.feature_landing.domain.entity.UpcomingEventData
import retrofit2.Response
import retrofit2.http.GET

interface RoomsEventApi {
    @GET("api/me/events?type=upcoming")
    suspend fun getUpcomingEventData(): Response<List<UpcomingEventData>>

    @GET("api/me/events?type=history")
    suspend fun getHistoryEvents(): Response<List<HistoryEventData>>
}