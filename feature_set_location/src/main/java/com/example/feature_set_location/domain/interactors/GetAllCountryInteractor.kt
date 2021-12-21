package com.example.feature_set_location.domain.interactors

import com.example.core_network.RequestResult

interface GetAllCountryInteractor {
    suspend fun getData(): RequestResult<List<String>>
}