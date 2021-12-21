package com.example.feature_set_location.domain.interactors_impl

import com.example.core_network.RequestResult
import com.example.feature_set_location.domain.interactors.GetUserOfficeCityInteractor
import com.example.feature_set_location.domain.repository.LocationRepository
import javax.inject.Inject

class GetUserOfficeCityInteractorImpl @Inject constructor(private val repository: LocationRepository) : GetUserOfficeCityInteractor {

    override suspend fun getData(): RequestResult<String> =
        when (val res = repository.getUserOffice()) {
            is RequestResult.Success -> RequestResult.Success(res.data.officeCity)
            is RequestResult.Error -> res
            is RequestResult.Loading -> res
        }
}