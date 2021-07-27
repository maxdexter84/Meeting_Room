package com.meetingroom.android.di

import com.meetingroom.android.di.network.ApiInterface
import dagger.Component
import retrofit2.Retrofit

@Component(modules = [CoreModule::class])
interface CoreComponent {
    fun retrofit(): Retrofit
    fun getApiInterface(): ApiInterface
}