package com.example.core_network

import dagger.Component
import retrofit2.Retrofit

@Component(modules = [NetworkModule::class])
interface NetworkComponent {
    fun retrofit(): Retrofit
}