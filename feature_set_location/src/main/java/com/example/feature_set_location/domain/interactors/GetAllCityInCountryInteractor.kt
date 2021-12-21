package com.example.feature_set_location.domain.interactors

import com.example.core_network.RequestResult

interface GetAllCityInCountryInteractor {
   suspend fun getData(country: String): RequestResult<List<String>>
}