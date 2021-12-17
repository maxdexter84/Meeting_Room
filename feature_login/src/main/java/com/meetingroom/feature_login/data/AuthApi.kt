package com.meetingroom.feature_login.data

import com.meetingroom.feature_login.domain.LoginRequest
import com.meetingroom.feature_login.domain.LoginResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/refresh")
    suspend fun refreshToken(
        @Header("Authorization") token: String
    ): Response<LoginResponseDto>

    @POST("api/auth/signin")
    suspend fun authenticateUser(@Body loginRequest: LoginRequest): Response<LoginResponseDto>
}