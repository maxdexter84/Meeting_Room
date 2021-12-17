package com.meetingroom.feature_login.domain

import com.example.core_network.RequestResult

interface ILoginRepository {
    suspend fun refreshToken(refToken: String): RequestResult<LoginResponseDto>
    suspend fun authenticateUser(loginRequest: LoginRequest): RequestResult<LoginResponseDto>
}