package com.example.feature_set_location.domain.interactors

interface SaveOfficeIdInteractor {
    suspend fun saveOfficeId(userOfficeId: Int)
}