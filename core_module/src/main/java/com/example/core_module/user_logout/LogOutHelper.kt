package com.example.core_module.user_logout

import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import kotlinx.datetime.Clock

class LogOutHelper(
    private val saveData: UserDataPrefHelperImpl
) {

    fun isDeleteRequired(): Boolean {
        val currentDayInMillis = Clock.System.now().toEpochMilliseconds()
        val tokenDayInMillis = saveData.getTokenDay() ?: return false
        val tokenExpirationTime = tokenDayInMillis + (DAY_LENGTH_IN_MILLIS * 7)
        return currentDayInMillis >= tokenExpirationTime
        return false
    }

    fun logout() {
        saveData.deleteToken()
    }

    private companion object {
        const val DAY_LENGTH_IN_MILLIS = 86400000
    }
}