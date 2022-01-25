package com.example.feature_set_location.domain.interactors

import com.example.core_network.RequestResult
import com.example.feature_set_location.domain.model.OfficeOfSelectedCountry

interface GetAllCityInCountryInteractor {
   suspend fun getData(country: String): RequestResult<List<OfficeOfSelectedCountry>>
}