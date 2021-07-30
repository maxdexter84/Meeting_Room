package com.example.core_network.user_responses

data class LogInResponse(
    val id: Int,
    val username: String,
    val email: String,
    val roles: List<String>,
    val tokenType: String,
    val accessToken: String
)