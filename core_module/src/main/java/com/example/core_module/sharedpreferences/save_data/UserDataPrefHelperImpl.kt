package com.example.core_module.sharedpreferences.save_data

import android.nfc.FormatException
import com.example.core_module.SharedpreferencesKeys.ACCESS_TOKEN_KEY
import com.example.core_module.SharedpreferencesKeys.CITY_OF_USER_LOCATION_KEY
import com.example.core_module.SharedpreferencesKeys.COUNTRY_OF_USER_LOCATION_KEY
import com.example.core_module.SharedpreferencesKeys.ROOM_OF_USER_SELECTION
import com.example.core_module.SharedpreferencesKeys.TOKEN_DAY_KEY
import com.example.core_module.SharedpreferencesKeys.USER_ROLES_KEY
import com.example.core_module.sharedpreferences.IPreferenceHelper
import kotlinx.datetime.Clock
import javax.inject.Inject

class UserDataPrefHelperImpl @Inject constructor(private val iPreferenceHelper: IPreferenceHelper) :
    UserDataPrefHelper {
    private var token: String? = null

    override fun saveToken(value: String) {
        if (value.length < 2) throw FormatException("Too short Token")
        if (token == null) {
            token = value
            iPreferenceHelper.saveString(ACCESS_TOKEN_KEY, value)
            iPreferenceHelper.saveLong(TOKEN_DAY_KEY, getCurrentDay())
        }
    }

    private fun getCurrentDay() = Clock.System.now().toEpochMilliseconds()

    override fun getTokenDay(): Long? = iPreferenceHelper.getLong(TOKEN_DAY_KEY)

    override fun getToken(): String? {
        if (token == null) {
            return null
        }
        return iPreferenceHelper.getString(ACCESS_TOKEN_KEY)
    }

    override fun saveCityOfUserLocation(value: String) {
        iPreferenceHelper.saveString(CITY_OF_USER_LOCATION_KEY, value)
    }

    override fun getCityOfUserLocation(): String? =
        iPreferenceHelper.getString(CITY_OF_USER_LOCATION_KEY)

    override fun saveCountryOfUserLocation(value: String) {
        iPreferenceHelper.saveString(COUNTRY_OF_USER_LOCATION_KEY, value)
    }

    override fun saveRoomOfUserSelection(value: String) {
        iPreferenceHelper.saveString(ROOM_OF_USER_SELECTION, value)
    }

    override fun getRoomOfUserSelection(): String? = iPreferenceHelper.getString(
        ROOM_OF_USER_SELECTION
    )

    override fun getCountryOfUserLocation(): String? =
        iPreferenceHelper.getString(COUNTRY_OF_USER_LOCATION_KEY)

    override fun deleteToken() {
        iPreferenceHelper.deleteUserToken(ACCESS_TOKEN_KEY, TOKEN_DAY_KEY)
    }

    override fun saveUserRoles(roles: Collection<String>) {
        iPreferenceHelper.saveCollectionAsStringSet(USER_ROLES_KEY, roles)
    }

    override fun getUserRoles(): Collection<String>? =
        iPreferenceHelper.getCollectionAsStringSet(USER_ROLES_KEY)

}