package com.example.feature_set_location.domain.mapers

import com.example.feature_set_location.domain.model.OfficeOfSelectedCountry
import com.example.feature_set_location.presentation.fragments.model.CityAdapterModel

object Mappers {

    fun mapCityListToCityAdapterModelList(list: List<OfficeOfSelectedCountry>, currentCity: String): List<CityAdapterModel> {
       return list.map { CityAdapterModel(it.city, it.id,  it.city == currentCity) }
    }
}