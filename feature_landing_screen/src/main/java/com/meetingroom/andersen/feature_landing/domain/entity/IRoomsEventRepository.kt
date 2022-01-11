package com.meetingroom.andersen.feature_landing.domain.entity

import com.example.core_network.RequestResult

interface IRoomsEventRepository {
    suspend fun getUpcomingEventData(): RequestResult<List<UpcomingEventData>>
    suspend fun getHistoryEvents(): RequestResult<List<HistoryEventData>>
    suspend fun putChangedEvent(event: ChangedEventDTO)
    suspend fun deleteUpcomingEvent(eventId: Long)
    suspend fun getRoomPickerNewEventData(startDateTime: String, endDateTime: String): RequestResult<Array<StatusRoomsDTO>>
}