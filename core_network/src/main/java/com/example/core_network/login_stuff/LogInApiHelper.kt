package com.example.core_network.login_stuff

import com.example.core_network.user_interfaces.LogInInterface
import com.example.core_network.user_posts.LogInRequest
import com.example.core_network.user_responses.LogInResponse
import retrofit2.Response

class LogInApiHelper constructor(private var apiService: LogInInterface) {

    suspend fun logInUser(logInRequest: LogInRequest): ResultOfLogIn<LogInResponse> {
        return safeApiCall(call = { apiService.logInUser(logInRequest) })
    }

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): ResultOfLogIn<T> {
        return try {
            val myResp = call.invoke()
            if (myResp.isSuccessful) {
                ResultOfLogIn.Success(myResp.body()!!)
            } else {
                ResultOfLogIn.Error(myResp.errorBody()?.string() ?: "Something goes wrong")
            }
        } catch (e: Exception) {
            ResultOfLogIn.Error(e.message ?: "Internet error runs")
        }
    }


}