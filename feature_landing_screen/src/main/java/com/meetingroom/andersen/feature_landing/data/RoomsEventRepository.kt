package com.meetingroom.andersen.feature_landing.data

import com.example.core_network.RequestMaker
import com.example.core_network.RequestResult
import com.meetingroom.andersen.feature_landing.domain.entity.HistoryEventData
import com.meetingroom.andersen.feature_landing.domain.entity.IRoomsEventRepository
import com.meetingroom.andersen.feature_landing.domain.entity.UpcomingEventData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomsEventRepository @Inject constructor(
    private val requestMaker: RequestMaker,
    private val roomsEventApi: RoomsEventApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): IRoomsEventRepository {
    override suspend fun getUpcomingEventData(): RequestResult<List<UpcomingEventData>> = withContext(ioDispatcher) {
        requestMaker.safeApiCall {
            roomsEventApi.getUpcomingEventData()
        }
    }

    override suspend fun getHistoryEvents(): RequestResult<List<HistoryEventData>> = withContext(ioDispatcher) {
        requestMaker.safeApiCall {
            roomsEventApi.getHistoryEvents()
        }
    }
}