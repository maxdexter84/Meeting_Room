package com.example.core_module.sharedpreferences.save_data

interface UserDataPrefHelper {
    fun saveToken(value: String)

    fun getToken(): String?
}