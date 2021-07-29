package com.meetingroom.android.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context): IPreferenceHelper {

    companion object {
        const val TOKEN = "token"
    }

    private val name = "SharedPreferencesHelper"
    private val preferences: SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    override fun setToken(token: String) {
       preferences[TOKEN] = token
    }

    override fun getToken(): String {
        return preferences[TOKEN] ?: ""
    }

    override fun clearPrefs() {
        preferences.edit().clear().apply()
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    private operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
        }
    }

    private inline operator fun <reified T : Any> SharedPreferences.get(
        key: String,
        defaultValue: T? = null
    ): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }
}