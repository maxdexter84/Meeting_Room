package com.example.feature_set_location.presentation.fragments.city_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper
import com.example.core_network.RequestResult
import com.example.feature_set_location.domain.interactors.GetAllCityInCountryInteractor
import com.example.feature_set_location.domain.mapers.Mappers
import com.example.feature_set_location.presentation.fragments.model.CityAdapterModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CityFragmentViewModel @Inject constructor(
    private val getAllCityInCountryInteractor: GetAllCityInCountryInteractor,
    private val prefHelper: UserDataPrefHelper
) : ViewModel() {

    private val _cityList = MutableStateFlow<List<CityAdapterModel>>(emptyList())
    val cityList = _cityList.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _cityList.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading = _cityList.asStateFlow()

     fun getCountryCityList(country: String) {
        viewModelScope.launch {
            when (val res = getAllCityInCountryInteractor.getData(country)) {
                is RequestResult.Success -> {
                    _loading.value = false
                    setCityList(res.data)
                }
                is RequestResult.Error -> {
                    _loading.value = false
                    _error.value = res.exception
                }
                is RequestResult.Loading -> _loading.value = true
            }
        }
    }

    private fun setCityList(list: List<String>) {
        val currentCity = prefHelper.getCityOfUserLocation() ?: ""
        _cityList.value = Mappers.mapCityListToCityAdapterModelList(list, currentCity)
    }

    fun saveCity(city: String){
        prefHelper.saveCityOfUserLocation(city)
    }

}
