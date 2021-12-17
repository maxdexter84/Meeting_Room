package com.example.core_network

import retrofit2.Response
import javax.inject.Inject

class RequestMaker @Inject constructor() {

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): RequestResult<T> {
        return try {
            val myResp = call.invoke()
            if (myResp.isSuccessful) {
                RequestResult.Success(myResp.body()!!)
            } else {
                RequestResult.Error(myResp.errorBody()?.toString() ?: "Something goes wrong", myResp.code())
            }
        } catch (e: Exception) {
            RequestResult.Error(e.message ?: "Internet error runs", DEFAULT_EXCEPTION_CODE)
        }
    }

    companion object {
        const val DEFAULT_EXCEPTION_CODE = 0
    }
}