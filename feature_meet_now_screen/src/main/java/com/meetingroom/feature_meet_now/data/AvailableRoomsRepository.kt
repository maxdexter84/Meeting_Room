package com.meetingroom.feature_meet_now.data

import com.example.core_network.RequestMaker
import com.example.core_network.RequestResult
import com.meetingroom.feature_meet_now.domain.entity.Room
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AvailableRoomsRepository @Inject constructor(
    private val requestMaker: RequestMaker,
    private val roomsApi: AvailableRoomsApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getAvailableRooms(time: String): RequestResult<List<Room>> =
        withContext(ioDispatcher) {
            requestMaker.safeApiCall {
                roomsApi.getAvailableRooms(time)
            }
        }
}