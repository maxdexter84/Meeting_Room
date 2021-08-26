package com.example.core_module.sharedpreferences.save_data

interface UserDataPrefHelper {
    fun saveToken(value: String)

    fun getToken(): String?

    fun saveCityOfUserLocation(value: String)

    fun getCityOfUserLocation(): String?

    fun saveCountryOfUserLocation(value: String)

    fun getCountryOfUserLocation(): String?

    fun getTokenDay(): Int?
}