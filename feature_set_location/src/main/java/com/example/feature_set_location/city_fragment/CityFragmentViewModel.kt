package com.example.feature_set_location.city_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import com.example.core_network.RequestMaker
import com.example.core_network.ResultOfRequest
import com.example.core_network.location_posts.GetAllAvailableCitiesRequest
import com.example.core_network.location_responses.GetAllAvailableCitiesResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

class CityFragmentViewModel @Inject constructor(
    private val requestMaker: RequestMaker,
    val saveData: UserDataPrefHelperImpl
) : ViewModel() {
    val requestResult: MutableLiveData<List<GetAllAvailableCitiesResponse>> by lazy {
        MutableLiveData<List<GetAllAvailableCitiesResponse>>()
    }

    fun tryToGetAllAvailableCities(country: GetAllAvailableCitiesRequest) {
        viewModelScope.launch {
            when (val request = requestMaker.getAllAvailableCountries(country)) {
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
