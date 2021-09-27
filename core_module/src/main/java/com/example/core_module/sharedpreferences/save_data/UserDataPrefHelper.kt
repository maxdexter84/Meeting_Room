package com.example.core_module.sharedpreferences.save_data

interface UserDataPrefHelper {
    fun saveToken(value: String)

    fun getToken(): String?

    fun saveCityOfUserLocation(value: String)

    fun getCityOfUserLocation(): String?

    fun saveCountryOfUserLocation(value: String)

    fun getCountryOfUserLocation(): String?

    fun saveRoomOfUserSelection(value: String)

    fun getRoomOfUserSelection() : String?

    fun saveTimeOfUserSelection(value: String)

    fun getTimeOfUserSelection() : String?

    fun getTokenDay(): Long?

    fun deleteToken()

    fun saveUserRoles(roles: Collection<String>)

    fun getUserRoles(): Collection<String>?
}