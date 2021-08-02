package com.meetingroom.android.di.save_data

interface ISaveNetworkData {
    fun saveToken(value: String)

    fun getToken(): String?
}