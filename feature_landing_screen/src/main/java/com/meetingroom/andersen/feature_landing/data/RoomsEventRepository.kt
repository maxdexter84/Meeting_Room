package com.meetingroom.andersen.feature_landing.data

import com.example.core_network.RequestMaker
import com.example.core_network.RequestResult
import com.meetingroom.andersen.feature_landing.domain.entity.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomsEventRepository @Inject constructor(
    private val requestMaker: RequestMaker,
    private val roomsEventApi: RoomsEventApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IRoomsEventRepository {
    override suspend fun getUpcomingEventData(): RequestResult<List<UpcomingEventDataDTO>> =
        withContext(ioDispatcher) {
            requestMaker.safeApiCall {
                roomsEventApi.getUpcomingEventData()
            }
        }

    override suspend fun getHistoryEvents(): RequestResult<List<HistoryEventDataDTO>> =
        withContext(ioDispatcher) {
            requestMaker.safeApiCall {
                roomsEventApi.getHistoryEvents()
            }
        }

    override suspend fun putChangedEvent(event: ChangedEventDTO) = withContext(ioDispatcher) {
        roomsEventApi.putChangedEvent(event)
    }

    override suspend fun deleteUpcomingEvent(eventId: Long) = withContext(ioDispatcher) {
        roomsEventApi.deleteUpcomingEvent(eventId)
    }

    override suspend fun putChangedEventForAdmin(event: ChangedEventDTO) = withContext(ioDispatcher) {
        roomsEventApi.putChangedEventForAdmin(event)
    }

    override suspend fun deleteUpcomingEventForAdmin(eventId: Long) = withContext(ioDispatcher) {
        roomsEventApi.deleteUpcomingEventForAdmin(eventId)
    }

    override suspend fun getRoomPickerNewEventData(
        dateTimeBody: DateTimeBody
    ): RequestResult<List<StatusRoomsDTO>> = withContext(ioDispatcher) {
        requestMaker.safeApiCall {
            roomsEventApi.getRoomPickerNewEventData(dateTimeBody)
        }
    }
}