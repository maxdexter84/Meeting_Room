package com.meetingroom.android.di

import android.content.Context
import com.example.core_network.NetworkModule
import com.meetingroom.android.ui.MainActivity
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Component(modules = [ApplicationModule::class, NetworkModule::class])
interface ApplicationComponent {
     fun appContext() : Context
     fun okHttpClient(): OkHttpClient
     fun retrofit(): Retrofit

     fun inject(activity: MainActivity)

}