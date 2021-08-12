package com.example.feature_set_location.city_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core_network.RequestMaker

class CityViewModelFactory(val requestMaker: RequestMaker) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        CityFragmentViewModel(requestMaker) as T
}
