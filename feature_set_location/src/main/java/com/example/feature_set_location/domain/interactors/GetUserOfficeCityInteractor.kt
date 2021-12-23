package com.example.feature_set_location.domain.interactors

import com.example.core_network.RequestResult

interface GetUserOfficeCityInteractor {

    suspend fun getData(): RequestResult<String>
}