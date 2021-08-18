package com.example.feature_set_location.country_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import com.example.core_network.RequestMaker

class CountryViewModelFactory(
    val requestMaker: RequestMaker,
    val saveData: UserDataPrefHelperImpl
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CountryFragmentViewModel(requestMaker, saveData) as T
}