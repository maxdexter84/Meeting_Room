package com.example.feature_set_location.city_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_network.ApiHelper
import com.example.core_network.ResultOfRequest
import com.example.core_network.location_posts.CountryPost
import com.example.core_network.location_responses.City
import kotlinx.coroutines.launch

class CityFragmentViewModel : ViewModel() {
    val requestResult: MutableLiveData<List<City>> by lazy {
        MutableLiveData<List<City>>()
    }

    fun tryToGetAllAvailableCities(country: CountryPost) {
        viewModelScope.launch {
            when (val request = ApiHelper.getAllAvailableCountries(country)) {
                is ResultOfRequest.Success -> {
                    request.data
                    requestResult.postValue(request.data)
                }
                is ResultOfRequest.Error -> {
                    requestResult.postValue(null)
                }

            }
        }

    }

}
