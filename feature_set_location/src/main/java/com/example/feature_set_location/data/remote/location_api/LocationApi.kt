package com.example.feature_set_location.data.remote.location_api

import com.example.feature_set_location.data.remote.dto.MyOffice
import com.example.feature_set_location.data.remote.dto.Office
import retrofit2.Response
import retrofit2.http.GET

interface LocationApi {

    @GET("/api/users/me")
    suspend fun getMyOffice(): Response<MyOffice>

    @GET("/api/offices")
    suspend fun getAllOffice(): Response<List<Office>>

}