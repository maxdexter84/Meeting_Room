package com.example.core_network

import com.example.core_network.user_interfaces.LogInInterface
import com.example.core_network.user_posts.LogInRequest
import com.example.core_network.user_responses.LogInResponse
import retrofit2.Response
import retrofit2.Retrofit

object ApiHelper {
    private var retrofit: Retrofit = DaggerNetworkComponent.create().retrofit()

    suspend fun logInUser(logInRequest: LogInRequest): ResultOfRequest<LogInResponse> {
        val serviceToLogIn = retrofit.create(LogInInterface::class.java)
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