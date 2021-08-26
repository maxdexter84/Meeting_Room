package com.example.core_module.user_logout

import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import java.util.*
import javax.inject.Inject

class LogoutUser @Inject constructor(
    private val saveData: UserDataPrefHelperImpl
) {
    fun deleteExpiredToken(): Boolean {
        val calendar = Calendar.getInstance()
        return true
    }

    fun logout() {

    }
}