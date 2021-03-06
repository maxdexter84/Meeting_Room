package com.example.core_module.sharedpreferences.pref_helper

interface IPreferenceHelper {

    fun saveString(key: String, value: String)

    fun getString(key: String): String?

    fun saveInt(key: String, value: Int)

    fun getInt(key: String): Int

    fun saveBoolean(key: String, value: Boolean)

    fun getBoolean(key: String): Boolean

    fun saveFloat(key: String, value: Float)

    fun getFloat(key: String): Float

    fun saveLong(key: String, value: Long)

    fun getLong(key: String): Long?

    fun clearPrefs()

    fun deleteUserToken(token: String, tokenDay: String)

    fun saveStringSet(key: String, collection: Set<String>)

    fun getStringSet(key: String): Set<String>?

    fun deleteTimeForReminder(eventId: Long)

}