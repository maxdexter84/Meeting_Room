package com.example.core_module.sharedpreferences.save_data

import android.nfc.FormatException
import com.example.core_module.sharedpreferences.IPreferenceHelper
import javax.inject.Inject

class SaveData @Inject constructor(private val iPreferenceHelper: IPreferenceHelper) :
    ISaveData {
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