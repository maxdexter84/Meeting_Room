package com.example.feature_set_location.country_fragment.interactors

import com.example.feature_set_location.country_fragment.entities.Country
import com.example.feature_set_location.country_fragment.network.CountryApi
import retrofit2.Retrofit
import javax.inject.Inject

class CountryInteractor @Inject constructor(val retrofit: Retrofit): ICountryInteractor {

    override suspend fun getAllCountries(): List<Country> {
        val api = retrofit.create(CountryApi::class.java)
        return api.getAllCountries()
    }
}