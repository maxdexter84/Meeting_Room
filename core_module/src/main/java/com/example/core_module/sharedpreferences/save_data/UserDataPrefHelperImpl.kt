package com.example.core_module.sharedpreferences.save_data

import android.nfc.FormatException
import com.example.core_module.sharedpreferences.IPreferenceHelper
import javax.inject.Inject

class UserDataPrefHelperImpl @Inject constructor(private val iPreferenceHelper: IPreferenceHelper) :
    UserDataPrefHelper {
    private var token: String? = null

    override fun saveToken(value: String) {
        if (value.length < 2) throw FormatException("Too short Token")
        if (token == null) {
            token = value
            iPreferenceHelper.saveString("token", value)
        }
    }

    override fun getToken(): String? {
        if (token == null) {
            return null
        }
        return iPreferenceHelper.getString("token")
    }

    override fun saveCityOfUserLocation(value: String) {
        iPreferenceHelper.saveString("city", value)
    }

    override fun getCityOfUserLocation(): String? = iPreferenceHelper.getString("city")

    override fun saveCountryOfUserLocation(value: String) {
        iPreferenceHelper.saveString("country", value)
    }

    override fun getCountryOfUserLocation(): String? = iPreferenceHelper.getString("country")
}