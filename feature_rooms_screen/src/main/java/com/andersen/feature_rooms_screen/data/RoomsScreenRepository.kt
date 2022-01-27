package com.andersen.feature_rooms_screen.data

import com.andersen.feature_rooms_screen.domain.entity.InRoomsScreenRepository
import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.domain.entity.remote.RoomsEventDTO
import com.example.core_network.RequestMaker
import com.example.core_network.RequestResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomsScreenRepository @Inject constructor(
    private val requestMaker: RequestMaker,
    private val roomsScreenApi: RoomsScreenApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : InRoomsScreenRepository {

    override suspend fun getRoomsApi(officeId: Int): RequestResult<List<Room>> =
        withContext(ioDispatcher) {
            requestMaker.safeApiCall {
                roomsScreenApi.getRoomsApi(officeId)
            }
        }

    override suspend fun getAllRoomsOnTheFloorApi(
        floor: String,
        officeId: Int
    ): RequestResult<List<Room>> = withContext(ioDispatcher) {
        requestMaker.safeApiCall {
            roomsScreenApi.getAllRoomsOnTheFloorApi(floor, officeId)
        }
    }

    override suspend fun getOneRoomApi(title: String): RequestResult<Room> =
        withContext(ioDispatcher) {
            requestMaker.safeApiCall {
                roomsScreenApi.getOneRoomApi(title)
            }
        }

    override suspend fun getRoomsEventsApi(date: String): RequestResult<List<RoomsEventDTO>> =
        withContext(ioDispatcher) {
            requestMaker.safeApiCall {
                roomsScreenApi.getRoomsEventsApi(date)
            }
        }

}