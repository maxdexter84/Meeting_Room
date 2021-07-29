package com.meetingroom.android.sharedpreferences

interface IPreferenceHelper {

    fun setToken(token: String)

    fun getToken(): String

    fun clearPrefs()
}