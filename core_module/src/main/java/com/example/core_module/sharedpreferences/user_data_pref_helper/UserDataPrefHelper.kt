package com.example.core_module.sharedpreferences.user_data_pref_helper

interface UserDataPrefHelper {

    var token: String?

    var refreshToken: String?

    fun getToken(tokenKey: String): String?

    fun saveCityOfUserLocation(value: String)

    fun getCityOfUserLocation(): String?

    fun saveOfficeIdOfUserLocation(value: Int)

    fun getOfficeIdOfUserLocation(): Int?

    fun saveCountryOfUserLocation(value: String)

    fun getCountryOfUserLocation(): String?

    fun getTokenDay(): Long?

    fun deleteToken()

    fun saveUserRole(role: String)

    fun getUserRole(): String?

    fun saveEventIdsForReminder(eventId: Set<String>)

    fun getEventIdsForReminder(): Set<String>?

    fun saveTimeForReminder(eventId: Long, time: String)

    fun getTimeForReminder(eventId: Long): String?

    fun deleteReminder(eventId: Long)
}