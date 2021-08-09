package com.example.core_module.sharedpreferences.save_data

interface ISaveData {
    fun saveToken(value: String)

    fun getToken(): String?
}