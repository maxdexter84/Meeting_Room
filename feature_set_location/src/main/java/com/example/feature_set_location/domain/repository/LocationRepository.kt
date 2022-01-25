package com.example.feature_set_location.domain.repository

import com.example.core_network.RequestResult
import com.example.feature_set_location.data.remote.dto.MyOffice
import com.example.feature_set_location.data.remote.dto.Office
import com.example.feature_set_location.data.remote.dto.UserUpdateOffice
import retrofit2.Response

interface LocationRepository {

    suspend fun getUserOffice(): RequestResult<MyOffice>
    suspend fun getAllOffice(): RequestResult<List<Office>>
    suspend fun saveOfficeId(userOfficeId: UserUpdateOffice): RequestResult<Unit>

}