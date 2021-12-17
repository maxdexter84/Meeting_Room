package com.meetingroom.feature_login.domain

data class LoginResponseDto(
    val refreshToken: String,
    val token: String
)