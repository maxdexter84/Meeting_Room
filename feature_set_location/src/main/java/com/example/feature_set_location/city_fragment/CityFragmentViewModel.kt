package com.example.feature_set_location.city_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper
import com.example.core_network.RequestMaker
import com.example.core_network.RequestResult
import com.example.core_network.location_interfaces.LocationInterface
import com.example.core_network.location_posts.GetAllAvailableCitiesRequest
import com.example.core_network.location_responses.GetAllAvailableCitiesResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

class CityFragmentViewModel @Inject constructor(
    private val requestMaker: RequestMaker,
    private val saveData: UserDataPrefHelper,
    private val locationApi: LocationInterface
) : ViewModel() {
    val requestResult: MutableLiveData<List<GetAllAvailableCitiesResponse>> by lazy {
        MutableLiveData<List<GetAllAvailableCitiesResponse>>()
    }

    init {
        tryToGetAllAvailableCities(GetAllAvailableCitiesRequest(getCountryOfUserLocation()!!))
    }

    fun tryToGetAllAvailableCities(country: GetAllAvailableCitiesRequest) {
        viewModelScope.launch {
            when (val request = locationApi.getAllAvailableCities(country)) {
                is RequestResult.Success -> {
                    request.data
                    requestResult.postValue(request.data)
                }
                is RequestResult.Error -> {
                    requestResult.postValue(null)
                }
            }
        }

    }

    fun saveCityOfUserLocation(cityName: String) {
        saveData.saveCityOfUserLocation(cityName)
    }

    fun getCityOfUserLocation(): String? = saveData.getCityOfUserLocation()

    fun getCountryOfUserLocation(): String? = saveData.getCountryOfUserLocation()

}
