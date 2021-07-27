package com.meetingroom.android.di.networkInterfaces

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    //TODO create entity to getSomething and add normal path
    @GET("PATH")
    fun getSomething(): Call<MutableList<String>>
}