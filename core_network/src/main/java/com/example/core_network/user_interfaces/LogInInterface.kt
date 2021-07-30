package com.example.core_network.user_interfaces

import com.example.core_network.user_posts.LogInRequest
import com.example.core_network.user_responses.LogInResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LogInInterface {
    @POST("signin")
    suspend fun logInUser(@Body post: LogInRequest): Response<LogInResponse>
}