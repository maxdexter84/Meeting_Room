package com.example.feature_set_location.domain.interactors_impl

import com.example.core_network.RequestResult
import com.example.feature_set_location.domain.interactors.GetAllCountryInteractor
import com.example.feature_set_location.domain.repository.LocationRepository
import javax.inject.Inject

class GetAllCountryInteractorImpl @Inject constructor(private val repository: LocationRepository) : GetAllCountryInteractor {

    override suspend fun getData(): RequestResult<List<String>> =
        when (val res = repository.getAllOffice()) {
            is RequestResult.Success -> RequestResult.Success(res.data.map { it.country }
                .toHashSet().toList())
            is RequestResult.Error -> res
            is RequestResult.Loading -> res
        }
}