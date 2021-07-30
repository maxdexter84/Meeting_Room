package com.meetingroom.android.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context): IPreferenceHelper {

    private val name = "SharedPreferencesHelper"
    private val preferences: SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    override fun saveString(key: String, value: String) {
       with(preferences.edit()) {
           putString(key, value).apply()
       }
    }

    override fun getString(key: String): String? {
        return preferences.getString(key, "")
    }

    override fun saveInt(key: String, value: Int) {
        with(preferences.edit()) {
            putInt(key, value).apply()
        }
    }

    override fun getInt(key: String): Int {
        return preferences.getInt(key, -1)
    }

    override fun saveBoolean(key: String, value: Boolean) {
        with(preferences.edit()) {
            putBoolean(key, value).apply()
        }
    }

    override fun getBoolean(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    override fun saveFloat(key: String, value: Float) {
        with(preferences.edit()) {
            putFloat(key, value).apply()
        }
    }

    override fun getFloat(key: String): Float {
        return preferences.getFloat(key, -1f)
    }

    override fun saveLong(key: String, value: Long) {
        with(preferences.edit()) {
            putLong(key, value).apply()
        }
    }

    override fun getLong(key: String): Long {
        return preferences.getLong(key, -1)
    }

    override fun clearPrefs() {
        preferences.edit().clear().apply()
    }
}