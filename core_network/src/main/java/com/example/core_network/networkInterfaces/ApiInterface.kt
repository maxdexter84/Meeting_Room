package com.example.core_network.networkInterfaces

import com.example.core_network.DaggerNetworkComponent
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton

interface ApiInterface {

    //TODO create entity to getSomething and add normal path
    @GET("PATH")
    fun getSomething(): Call<MutableList<String>>
}