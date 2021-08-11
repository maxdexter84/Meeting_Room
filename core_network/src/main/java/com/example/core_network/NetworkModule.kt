package com.example.core_network

import com.example.core_network.location_interfaces.LocationInterface
import com.example.core_network.user_interfaces.LogInInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @Provides
    fun getLocationInterface(retrofit: Retrofit):LocationInterface{
        return GagForInternetsRequests()
    }

    @Provides
    fun getLogInInterface(retrofit: Retrofit): LogInInterface {
        return GagForInternetsRequests()
//      return retrofit.create(LogInInterface::class.java)
    }

    @Provides
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun getOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return httpLoggingInterceptor
    }

    companion object {
        private const val BASE_URL = "http://178.62.212.6:8080/api/auth/"
    }
}