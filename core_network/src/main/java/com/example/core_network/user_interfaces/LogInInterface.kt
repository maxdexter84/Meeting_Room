package com.example.core_network.user_interfaces

import com.example.core_network.user_posts.LogInPost
import com.example.core_network.user_responses.LogInResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LogInInterface {
    @POST("signin")
    fun logInUser(@Body post: LogInPost): Call<LogInResponse>
}