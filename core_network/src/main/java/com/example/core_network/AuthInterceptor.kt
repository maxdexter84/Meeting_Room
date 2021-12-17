package com.example.core_network

import com.example.core_module.sharedpreferences.SharedPreferencesKeys
import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    @Inject
    lateinit var userDataPrefHelper: UserDataPrefHelper

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val accessToken = userDataPrefHelper.getToken(SharedPreferencesKeys.ACCESS_TOKEN_KEY)
        if (!accessToken.isNullOrBlank()) {
            requestBuilder.addHeader(
                HEADER_NAME,
                accessToken
            )
        }
        return chain.proceed(requestBuilder.build())
    }

    companion object {
        private const val HEADER_NAME = "Authorization"
    }
}
