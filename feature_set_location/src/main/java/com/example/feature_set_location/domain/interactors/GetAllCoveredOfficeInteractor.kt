package com.example.feature_set_location.domain.interactors

import com.example.core_network.RequestResult

interface GetAllCoveredOfficeInteractor {
    suspend fun getData(): RequestResult<List<String>>
}