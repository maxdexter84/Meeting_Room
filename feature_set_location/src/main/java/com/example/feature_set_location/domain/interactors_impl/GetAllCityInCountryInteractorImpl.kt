package com.example.feature_set_location.domain.interactors_impl

import com.example.core_network.RequestResult
import com.example.feature_set_location.domain.interactors.GetAllCityInCountryInteractor
import com.example.feature_set_location.domain.repository.LocationRepository
import javax.inject.Inject

class GetAllCityInCountryInteractorImpl @Inject constructor(private val repository: LocationRepository) : GetAllCityInCountryInteractor {

    override suspend fun getData(country: String): RequestResult<List<String>> =
        when (val res = repository.getAllOffice()) {
            is RequestResult.Success -> RequestResult.Success(res.data.filter { it.country == country }.map { it.city })
            is RequestResult.Error -> res
            is RequestResult.Loading -> res
        }

}