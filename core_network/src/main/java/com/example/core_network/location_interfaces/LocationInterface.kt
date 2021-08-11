package com.example.core_network.location_interfaces

import com.example.core_network.location_posts.CountryPost
import com.example.core_network.location_responses.City
import com.example.core_network.location_responses.Country
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LocationInterface {

    @GET("countries")
    suspend fun getAllAvailableCountries(): Response<List<CountryPost>>

    @POST("cities")
    suspend fun getAllAvailableCities(@Body post: CountryPost): Response<List<City>>
}