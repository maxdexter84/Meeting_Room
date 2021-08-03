package com.example.sharedpreferences.sharedpreferences.save_data

import android.nfc.FormatException
import com.example.sharedpreferences.sharedpreferences.IPreferenceHelper
import javax.inject.Inject

class SaveNetworkData @Inject constructor(private val iPreferenceHelper: IPreferenceHelper) :
    ISaveNetworkData {
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
}