package com.meetingroom.android.di

import com.meetingroom.android.network.ApiInterface
import com.meetingroom.android.network.NetworkModule
import dagger.Component
import retrofit2.Retrofit

@Component(modules = [ApplicationModule::class, NetworkModule::class])
interface ApplicationComponent {
    fun retrofit(): Retrofit
    fun getApiInterface(): ApiInterface
}