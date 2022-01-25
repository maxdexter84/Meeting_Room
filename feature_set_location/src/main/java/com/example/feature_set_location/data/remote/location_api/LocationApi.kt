package com.example.feature_set_location.data.remote.location_api

import com.example.feature_set_location.data.remote.dto.MyOffice
import com.example.feature_set_location.data.remote.dto.Office
import com.example.feature_set_location.data.remote.dto.UserUpdateOffice
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface LocationApi {

    @GET("/api/users/me")
    suspend fun getMyOffice(): Response<MyOffice>

    @GET("/api/offices")
    suspend fun getAllOffice(): Response<List<Office>>

    @PATCH("/api/users/me/office")
    suspend fun saveOfficeId(@Body userOfficeId: UserUpdateOffice): Response<Unit>
}