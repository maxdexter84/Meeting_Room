package com.example.feature_set_location.domain.interactors_impl

import com.example.core_network.RequestResult
import com.example.feature_set_location.domain.interactors.GetAllCoveredOfficeInteractor
import com.example.feature_set_location.domain.repository.LocationRepository
import javax.inject.Inject

class GetAllCoveredOfficeInteractorImpl @Inject constructor(private val repository: LocationRepository) : GetAllCoveredOfficeInteractor {

    override suspend fun getData(): RequestResult<List<String>> =
        when (val res = repository.getAllOffice()) {
            is RequestResult.Success -> RequestResult.Success(res.data.map { it.city })
            is RequestResult.Error -> res
            is RequestResult.Loading -> res
        }

}