package com.example.feature_set_location.country_fragment.network

import com.example.feature_set_location.country_fragment.entities.Country
import retrofit2.http.GET

interface CountryApi {

    @GET("signin")
    suspend fun getAllCountries(): List<Country>
}