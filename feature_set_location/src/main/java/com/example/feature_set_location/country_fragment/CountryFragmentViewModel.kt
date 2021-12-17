package com.example.feature_set_location.country_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper
import com.example.core_network.RequestMaker
import com.example.core_network.RequestResult
import com.example.core_network.location_interfaces.LocationInterface
import com.example.core_network.location_responses.GetAllAvailableCountriesResponse
import kotlinx.coroutines.launch
import javax.inject.Inject


class CountryFragmentViewModel @Inject constructor(
    private val requestMaker: RequestMaker,
    private val saveData: UserDataPrefHelper,
    private val locationApi: LocationInterface
) : ViewModel() {
    val requestResult: MutableLiveData<List<GetAllAvailableCountriesResponse>> by lazy {
        MutableLiveData<List<GetAllAvailableCountriesResponse>>()
    }

    init {
        tryToGetAllAvailableCountries()
    }

    fun tryToGetAllAvailableCountries() {
        viewModelScope.launch {
            when (val request = locationApi.getAllAvailableCountries()) {
                is RequestResult.Success -> {
                    requestResult.postValue(request.data)
                }
                is RequestResult.Error -> {
                    requestResult.postValue(null)
                }

            }
        }

    }

    fun saveCountryOfUserLocation(countryName: String) {
        saveData.saveCountryOfUserLocation(countryName)
    }

}