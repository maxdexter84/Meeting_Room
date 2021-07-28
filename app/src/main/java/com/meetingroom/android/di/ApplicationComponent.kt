package com.meetingroom.android.di

import com.example.core_network.NetworkModule
import com.example.core_network.networkInterfaces.ApiInterface
import dagger.Component
import retrofit2.Retrofit

@Component(modules = [ApplicationModule::class, NetworkModule::class])
interface ApplicationComponent {
    fun retrofit(): Retrofit
}