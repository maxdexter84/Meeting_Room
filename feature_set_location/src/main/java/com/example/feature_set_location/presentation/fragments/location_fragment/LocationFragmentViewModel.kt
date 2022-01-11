package com.example.feature_set_location.presentation.fragments.location_fragment

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper
import com.example.core_network.RequestResult
import com.example.feature_set_location.domain.interactors.GetAllCoveredOfficeInteractor
import com.example.feature_set_location.domain.interactors.GetUserOfficeCityInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationFragmentViewModel @Inject constructor(
    private val getUserOfficeCity: GetUserOfficeCityInteractor,
    private val getAllCoveredOfficeInteractor: GetAllCoveredOfficeInteractor,
    private val prefHelper: UserDataPrefHelper
) : ViewModel() {

    private val _myOffice = MutableStateFlow(EMPTY_STRING)
    val myOffice = _myOffice.asSharedFlow()

    private val _error = MutableStateFlow("")
    val error = _error.asSharedFlow()

    private val _loading = MutableStateFlow(true)
    val loading = _loading.asSharedFlow()

    private val _covered = MutableStateFlow(true)
    val covered = _covered.asSharedFlow()

    init {
        getUserCity()
        saveDataAboutRole()
    }

    private fun getUserCity() {
        val city = prefHelper.getCityOfUserLocation() ?: EMPTY_STRING
        if (city != "") _myOffice.value = city
        else {
            viewModelScope.launch {
                when (val res = getUserOfficeCity.getData()) {
                    is RequestResult.Success -> {
                        _myOffice.value = res.data
                        _loading.value = false
                        prefHelper.saveCityOfUserLocation(res.data)
                    }
                    is RequestResult.Error -> {
                        _error.value = res.exception
                        _loading.value = false
                    }
                    is RequestResult.Loading -> _loading.value = true
                }
            }
        }
        checkCoveredOffice(_myOffice.value)
    }

    private fun saveDataAboutRole() {
            viewModelScope.launch {
                when (val response = getUserOfficeCity.getRole()) {
                    is RequestResult.Success -> {
                        val roles = prefHelper.getUserRoles()
                        val currentRole = response.data
                        if (roles != null) {
                            roles.forEach {
                                if(it != currentRole){
                                    roles.toMutableList().add(currentRole)
                                    prefHelper.saveUserRoles(roles)
                                }
                            }
                        } else {
                            prefHelper.saveUserRoles(listOf(currentRole))
                        }
                    }
                    is RequestResult.Error -> {
                        _error.value = response.exception
                        _loading.value = false
                    }
                    is RequestResult.Loading -> _loading.value = true
                }
            }
    }

    private fun checkCoveredOffice(city: String) {
        viewModelScope.launch {
            when (val res = getAllCoveredOfficeInteractor.getData()) {
                is RequestResult.Success -> {
                    _covered.emit(res.data.contains(city))
                    _loading.value = false
                }
                is RequestResult.Error -> {
                    _error.value = res.exception
                    _loading.value = false
                }
                is RequestResult.Loading -> _loading.value = true
            }
        }
    }

    companion object{
        const val EMPTY_STRING = ""
    }
}