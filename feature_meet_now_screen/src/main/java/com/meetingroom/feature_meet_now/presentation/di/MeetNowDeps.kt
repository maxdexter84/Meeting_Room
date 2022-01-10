package com.meetingroom.feature_meet_now.presentation.di

import com.example.core_network.RequestMaker
import retrofit2.Retrofit

interface MeetNowDeps {
    fun requestMaker(): RequestMaker
    fun retrofit(): Retrofit
}