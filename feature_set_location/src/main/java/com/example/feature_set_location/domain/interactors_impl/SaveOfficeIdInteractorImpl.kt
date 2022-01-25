package com.example.feature_set_location.domain.interactors_impl

import com.example.feature_set_location.data.remote.dto.UserUpdateOffice
import com.example.feature_set_location.domain.interactors.SaveOfficeIdInteractor
import com.example.feature_set_location.domain.repository.LocationRepository
import javax.inject.Inject

class SaveOfficeIdInteractorImpl @Inject constructor(
    private val repository: LocationRepository
) : SaveOfficeIdInteractor {

    override suspend fun saveOfficeId(userOfficeId: Int) {
        repository.saveOfficeId(UserUpdateOffice(userOfficeId))
    }
}