package com.example.feature_set_location.country_fragment.interactors

import com.example.feature_set_location.country_fragment.entities.Country

interface ICountryInteractor {

    suspend fun getAllCountries(): List<Country>
}