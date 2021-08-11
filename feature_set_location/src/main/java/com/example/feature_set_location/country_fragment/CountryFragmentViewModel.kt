package com.example.feature_set_location.country_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_network.ApiHelper
import com.example.core_network.ResultOfRequest
import com.example.core_network.location_posts.CountryPost
import kotlinx.coroutines.launch

class CountryFragmentViewModel : ViewModel() {
    val requestResult: MutableLiveData<List<CountryPost>> by lazy {
        MutableLiveData<List<CountryPost>>()
    }

    fun tryToGetAllAvailableCountries() {
        viewModelScope.launch {
            when (val request = ApiHelper.getAllAvailableCities()) {
                is ResultOfRequest.Success -> {
                    requestResult.postValue(request.data)
                }
                is ResultOfRequest.Error -> {
                    requestResult.postValue(null)
                }

            }
        }

    }

}