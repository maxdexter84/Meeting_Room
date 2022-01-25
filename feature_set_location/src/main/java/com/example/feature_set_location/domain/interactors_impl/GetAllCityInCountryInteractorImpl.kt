package com.example.feature_set_location.domain.interactors_impl

import com.example.core_network.RequestResult
import com.example.feature_set_location.domain.interactors.GetAllCityInCountryInteractor
import com.example.feature_set_location.domain.model.OfficeOfSelectedCountry
import com.example.feature_set_location.domain.repository.LocationRepository
import javax.inject.Inject

class GetAllCityInCountryInteractorImpl @Inject constructor(private val repository: LocationRepository) : GetAllCityInCountryInteractor {

    override suspend fun getData(country: String): RequestResult<List<OfficeOfSelectedCountry>> =
        when (val res = repository.getAllOffice()) {
            is RequestResult.Success -> RequestResult.Success(res.data.filter {
                it.country == country
            }.map {
                OfficeOfSelectedCountry(it.city, it.id)
            })
            is RequestResult.Error -> res
            is RequestResult.Loading -> res
        }

}