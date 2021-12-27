package com.meetingroom.andersen.feature_landing.domain.entity

import com.example.core_network.RequestResult

interface IRoomsEventRepository {
    suspend fun getUpcomingEventData(): RequestResult<List<UpcomingEventData>>
    suspend fun getHistoryEvents(): RequestResult<List<HistoryEventData>>
}