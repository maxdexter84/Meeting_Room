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
    override suspend fun getUpcomingEventData(): RequestResult<List<UpcomingEventData>> =
        withContext(ioDispatcher) {
            requestMaker.safeApiCall {
                roomsEventApi.getUpcomingEventData()
            }
        }

    override suspend fun getHistoryEvents(): RequestResult<List<HistoryEventData>> =
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
        startDateTime: String,
        endDateTime: String
    ): RequestResult<Array<StatusRoomsDTO>> {
        val arrayRooms = generationGagForRooms()
        return RequestResult.Success(arrayRooms.sortedWith(
                compareBy(
                    { room -> !room.isEnabled },
                    { room -> room.title }
                )
            )
            .toTypedArray())
    }

    private fun generationGagForRooms(): Array<StatusRoomsDTO> {
        return Array(6) { i ->
            val roomId = when (i) {
                0 -> 0
                1 -> 1
                2 -> 2
                3 -> 3
                4 -> 4
                5 -> 5
                6 -> 6
                7 -> 7
                else -> 8
            }

            val room = when (i) {
                0 -> "Gray"
                1 -> "Blue"
                2 -> "Green"
                3 -> "Black"
                4 -> "Drkgray"
                5 -> "Magenta"
                6 -> "Red"
                7 -> "Yellow"
                else -> "Green"
            }

            val isSelected = false

            val isEnabled = when (i) {
                3, 4 -> false
                else -> true
            }
            StatusRoomsDTO(roomId.toLong(), room, isSelected, isEnabled)
        }
    }
}