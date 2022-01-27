package com.example.core_module.sharedpreferences.user_data_pref_helper

import com.example.core_module.sharedpreferences.SharedPreferencesKeys
import com.example.core_module.sharedpreferences.SharedPreferencesKeys.ACCESS_TOKEN_KEY
import com.example.core_module.sharedpreferences.SharedPreferencesKeys.CITY_OF_USER_LOCATION_KEY
import com.example.core_module.sharedpreferences.SharedPreferencesKeys.COUNTRY_OF_USER_LOCATION_KEY
import com.example.core_module.sharedpreferences.SharedPreferencesKeys.EVENT_IDS_FOR_REMINDER_KEY
import com.example.core_module.sharedpreferences.SharedPreferencesKeys.OFFICE_ID_OF_USER_LOCATION_KEY
import com.example.core_module.sharedpreferences.SharedPreferencesKeys.REFRESH_TOKEN_KEY
import com.example.core_module.sharedpreferences.SharedPreferencesKeys.TOKEN_DAY_KEY
import com.example.core_module.sharedpreferences.SharedPreferencesKeys.USER_ID_KEY
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

    override fun saveCityOfUserLocation(value: String) =
        iPreferenceHelper.saveString(CITY_OF_USER_LOCATION_KEY, value)

    override fun getCityOfUserLocation() = iPreferenceHelper.getString(CITY_OF_USER_LOCATION_KEY)

    override fun saveOfficeIdOfUserLocation(value: Int) {
        iPreferenceHelper.saveInt(OFFICE_ID_OF_USER_LOCATION_KEY, value)
    }

    override fun getOfficeIdOfUserLocation() = iPreferenceHelper.getInt(OFFICE_ID_OF_USER_LOCATION_KEY)

    override fun saveCountryOfUserLocation(value: String) =
        iPreferenceHelper.saveString(COUNTRY_OF_USER_LOCATION_KEY, value)

    override fun getCountryOfUserLocation() =
        iPreferenceHelper.getString(COUNTRY_OF_USER_LOCATION_KEY)

    override fun saveUserId(value: Int) {
        iPreferenceHelper.saveInt(USER_ID_KEY, value)
    }

    override fun deleteToken() = iPreferenceHelper.deleteUserToken(ACCESS_TOKEN_KEY, TOKEN_DAY_KEY)

    override fun saveUserRole(role: String) {
        iPreferenceHelper.saveString(USER_ROLES_KEY, role)
    }

    override fun getUserRole() =
        iPreferenceHelper.getString(USER_ROLES_KEY)

    override fun saveEventIdsForReminder(eventId: Set<String>) {
        iPreferenceHelper.saveStringSet(EVENT_IDS_FOR_REMINDER_KEY, eventId)
    }

    override fun getEventIdsForReminder(): Set<String>? =
        iPreferenceHelper.getStringSet(EVENT_IDS_FOR_REMINDER_KEY)?.toSet()

    override fun saveTimeForReminder(eventId: Long, time: String) {
        iPreferenceHelper.saveString(eventId.toString(), time)
    }

    override fun getTimeForReminder(eventId: Long): String? =
        iPreferenceHelper.getString(eventId.toString())

    override fun deleteReminder(eventId: Long) {
        iPreferenceHelper.deleteTimeForReminder(eventId)
    }

    override fun getTheme(): Int {
        return iPreferenceHelper.getInt(SharedPreferencesKeys.KEY_THEME)
    }

    override fun saveTheme(key: String, theme: Int) {
        iPreferenceHelper.saveInt(key, theme)
    }

}