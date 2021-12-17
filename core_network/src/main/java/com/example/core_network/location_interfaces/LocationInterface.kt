package com.example.core_network.location_interfaces

import com.example.core_network.RequestResult
import com.example.core_network.location_posts.GetAllAvailableCitiesRequest
import com.example.core_network.location_responses.GetAllAvailableCitiesResponse
import com.example.core_network.location_responses.GetAllAvailableCountriesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LocationInterface {

    @GET("countries")
    suspend fun getAllAvailableCountries(): RequestResult<List<GetAllAvailableCountriesResponse>>

    @POST("cities")
    suspend fun getAllAvailableCities(@Body post: GetAllAvailableCitiesRequest): RequestResult<List<GetAllAvailableCitiesResponse>>
}