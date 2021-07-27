package com.meetingroom.android.di

import com.meetingroom.android.di.networkInterfaces.ApiInterface
import dagger.Component
import retrofit2.Retrofit

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun retrofit(): Retrofit
    fun getApiInterface(): ApiInterface
}