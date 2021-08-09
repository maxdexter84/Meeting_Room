package com.example.core_module.sharedpreferences.save_data

interface ISaveNetworkData {
    fun saveToken(value: String)

    fun getToken(): String?
}