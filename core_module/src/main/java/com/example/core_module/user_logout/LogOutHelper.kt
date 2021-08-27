package com.example.core_module.user_logout

import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import java.util.*

class LogOutHelper(
    private val saveData: UserDataPrefHelperImpl
) {
    fun deleteExpiredToken(): Boolean {
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val tokenDay = saveData.getTokenDay() ?: return false

        return if (tokenDay - currentDay >= 7) {
            saveData.deleteToken()
            true
        } else {
            false
        }
    }

    fun logout() {
        saveData.deleteToken()
    }
}