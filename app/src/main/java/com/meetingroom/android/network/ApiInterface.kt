package com.meetingroom.android.network

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    //TODO create entity to getSomething and add normal path
    @GET("PATH")
    fun getSomething(): Call<MutableList<String>>
}