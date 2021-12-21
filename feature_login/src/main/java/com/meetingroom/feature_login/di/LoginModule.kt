package com.meetingroom.feature_login.di

import com.example.core_network.RequestMaker
import com.meetingroom.feature_login.data.AuthApi
import com.meetingroom.feature_login.data.LoginRepository
import com.meetingroom.feature_login.domain.ILoginRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class LoginModule() {

    @Provides
    fun getLoginApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    fun getLoginRepository(requestMaker: RequestMaker, authApi: AuthApi): ILoginRepository =
        LoginRepository(requestMaker, authApi)
}