package com.example.core_network

import com.example.core_network.user_interfaces.LogInInterface
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Component(modules = [NetworkModule::class])
interface NetworkComponent {
    fun retrofit(): Retrofit
    fun okHttpClient(): OkHttpClient
    fun logInInterface():LogInInterface
    fun gagForInternetsRequests(): GagForInternetsRequests
}