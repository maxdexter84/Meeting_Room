package com.example.feature_set_location.presentation.fragments.country_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_network.RequestResult
import com.example.feature_set_location.domain.interactors.GetAllCountryInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CountryFragmentViewModel @Inject constructor(
    private val getAllCountryInteractor: GetAllCountryInteractor
) : ViewModel() {
    private val _countryList = MutableStateFlow<List<String>>(emptyList())
    val countryList = _countryList.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _countryList.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading = _countryList.asStateFlow()

    init {
        getCountryList()
    }

    private fun getCountryList() {
        viewModelScope.launch {
            when (val res = getAllCountryInteractor.getData()) {
                is RequestResult.Success -> {
                    _loading.value = false
                    _countryList.value = res.data
                }
                is RequestResult.Error -> {
                    _loading.value = false
                    _error.value = res.exception
                }
                is RequestResult.Loading -> _loading.value = true
            }
        }

    }
}