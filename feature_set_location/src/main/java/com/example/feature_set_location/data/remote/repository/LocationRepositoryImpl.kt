package com.example.feature_set_location.data.remote.repository

import com.example.core_network.RequestMaker
import com.example.core_network.RequestResult
import com.example.feature_set_location.data.remote.dto.MyOffice
import com.example.feature_set_location.data.remote.dto.Office
import com.example.feature_set_location.data.remote.location_api.LocationApi
import com.example.feature_set_location.domain.repository.LocationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val requestMaker: RequestMaker,
    private val api: LocationApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocationRepository {

    override suspend fun getUserOffice(): RequestResult<MyOffice> = withContext(ioDispatcher) {
        requestMaker.safeApiCall {
            api.getMyOffice()
        }
    }

    override suspend fun getAllOffice(): RequestResult<List<Office>> = withContext(ioDispatcher) {
        requestMaker.safeApiCall {
            api.getAllOffice()
        }
    }

}