package com.example.sharedpreferences.sharedpreferences.save_data

interface ISaveNetworkData {
    fun saveToken(value: String)

    fun getToken(): String?
}