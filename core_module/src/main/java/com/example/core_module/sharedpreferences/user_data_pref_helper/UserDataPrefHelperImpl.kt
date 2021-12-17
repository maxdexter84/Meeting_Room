package com.example.core_module.sharedpreferences.user_data_pref_helper

import com.example.core_module.sharedpreferences.SharedPreferencesKeys.ACCESS_TOKEN_KEY
import com.example.core_module.sharedpreferences.SharedPreferencesKeys.CITY_OF_USER_LOCATION_KEY
import com.example.core_module.sharedpreferences.SharedPreferencesKeys.COUNTRY_OF_USER_LOCATION_KEY
import com.example.core_module.sharedpreferences.SharedPreferencesKeys.REFRESH_TOKEN_KEY
import com.example.core_module.sharedpreferences.SharedPreferencesKeys.TOKEN_DAY_KEY
import com.example.core_module.sharedpreferences.SharedPreferencesKeys.USER_ROLES_KEY
import com.example.core_module.sharedpreferences.pref_helper.IPreferenceHelper
import kotlinx.datetime.Clock
import javax.inject.Inject

class UserDataPrefHelperImpl @Inject constructor(private val iPreferenceHelper: IPreferenceHelper) :
    UserDataPrefHelper {

    override var token: String? = null
        set(value) {
            field = value
            if (value != null) {
                iPreferenceHelper.saveString(ACCESS_TOKEN_KEY, value)
                iPreferenceHelper.saveLong(TOKEN_DAY_KEY, getCurrentDay())
            }
        }

    override var refreshToken: String? = null
        set(value) {
            field = value
            if (value != null) {
                iPreferenceHelper.saveString(REFRESH_TOKEN_KEY, value)
            }
        }

    private fun getCurrentDay() = Clock.System.now().toEpochMilliseconds()

    override fun getToken(tokenKey: String) = iPreferenceHelper.getString(tokenKey)

    override fun getTokenDay(): Long? = iPreferenceHelper.getLong(TOKEN_DAY_KEY)

    override fun saveCityOfUserLocation(value: String) = iPreferenceHelper.saveString(CITY_OF_USER_LOCATION_KEY, value)

    override fun getCityOfUserLocation() = iPreferenceHelper.getString(CITY_OF_USER_LOCATION_KEY)

    override fun saveCountryOfUserLocation(value: String) =
        iPreferenceHelper.saveString(COUNTRY_OF_USER_LOCATION_KEY, value)

    override fun getCountryOfUserLocation() = iPreferenceHelper.getString(COUNTRY_OF_USER_LOCATION_KEY)

    override fun deleteToken() = iPreferenceHelper.deleteUserToken(ACCESS_TOKEN_KEY, TOKEN_DAY_KEY)

    override fun saveUserRoles(roles: Collection<String>) = iPreferenceHelper.saveCollectionAsStringSet(USER_ROLES_KEY, roles)

    override fun getUserRoles(): Collection<String>? =
        iPreferenceHelper.getCollectionAsStringSet(USER_ROLES_KEY)
}