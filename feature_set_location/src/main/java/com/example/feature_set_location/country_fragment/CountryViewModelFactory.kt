package com.example.feature_set_location.country_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CountryViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CountryFragmentViewModel() as T
}