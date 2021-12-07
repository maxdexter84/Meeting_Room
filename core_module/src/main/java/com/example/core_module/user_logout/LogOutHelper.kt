package com.example.core_module.user_logout

import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelper
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import kotlinx.datetime.Clock
import javax.inject.Inject

class LogOutHelper @Inject constructor (
    private val saveData: UserDataPrefHelper
) {

    fun isDeleteRequired(): Boolean {
        val currentDayInMillis = Clock.System.now().toEpochMilliseconds()
        val tokenDayInMillis = saveData.getTokenDay() ?: return false
        val tokenExpirationTime = tokenDayInMillis + (DAY_LENGTH_IN_MILLIS * 7)
        return currentDayInMillis >= tokenExpirationTime
    }

    fun logout() {
        saveData.deleteToken()
    }

    private companion object {
        const val DAY_LENGTH_IN_MILLIS = 86400000
    }
}