package com.example.core_network

import com.example.core_network.user_posts.LogInRequest
import com.example.core_network.user_responses.LogInResponse
import retrofit2.Response

object ApiHelper {
    suspend fun logInUser(logInRequest: LogInRequest): ResultOfRequest<LogInResponse> {
        val serviceToLogIn = DaggerNetworkComponent.create().logInInterface()
        return safeApiCall(call = { serviceToLogIn.logInUser(logInRequest) })
    }

    private suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): ResultOfRequest<T> {
        return try {
            val myResp = call.invoke()
            if (myResp.isSuccessful) {
                ResultOfRequest.Success(myResp.body()!!)
            } else {
                ResultOfRequest.Error(myResp.errorBody()?.string() ?: "Something goes wrong")
            }
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: "Internet error runs")
        }
    }
}