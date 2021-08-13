package com.example.feature_set_location.country_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_network.RequestMaker
import com.example.core_network.ResultOfRequest
import com.example.core_network.location_responses.GetAllAvailableCountriesResponse
import kotlinx.coroutines.launch
import javax.inject.Inject


class CountryFragmentViewModel @Inject constructor(
    private val requestMaker: RequestMaker
) : ViewModel() {
    val requestResult: MutableLiveData<List<GetAllAvailableCountriesResponse>> by lazy {
        MutableLiveData<List<GetAllAvailableCountriesResponse>>()
    }


    fun tryToGetAllAvailableCountries() {
        viewModelScope.launch {
            when (val request = requestMaker.getAllAvailableCountries()) {
                is ResultOfRequest.Success -> {
                    requestResult.postValue(request.data)
                }
                is ResultOfRequest.Error -> {
                    requestResult.postValue(null)
                }

            }
        }

    }

    val selected = MutableLiveData<String>()

    fun select(country: String) {
        selected.value = country
    }

    fun getSelected(): LiveData<String>? {
        return selected
    }

}