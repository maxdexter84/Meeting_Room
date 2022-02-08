package com.meetingroom.andersen.feature_landing.domain.entity

import com.example.core_network.RequestResult

interface IRoomsEventRepository {
    suspend fun getUpcomingEventData(): RequestResult<List<UpcomingEventDataDTO>>
    suspend fun getHistoryEvents(): RequestResult<List<HistoryEventDataDTO>>
    suspend fun putChangedEvent(event: ChangedEventDTO)
    suspend fun deleteUpcomingEvent(eventId: Long)
    suspend fun putChangedEventForAdmin(event: ChangedEventDTO)
    suspend fun deleteUpcomingEventForAdmin(eventId: Long)
    suspend fun getRoomPickerNewEventData(dateTimeBody: DateTimeBody): RequestResult<List<StatusRoomsDTO>>
}