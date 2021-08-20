package com.example.core_network

import com.example.core_network.location_interfaces.LocationInterface
import com.example.core_network.location_posts.GetAllAvailableCitiesRequest
import com.example.core_network.location_responses.GetAllAvailableCitiesResponse
import com.example.core_network.location_responses.GetAllAvailableCountriesResponse
import com.example.core_network.user_interfaces.LogInInterface
import com.example.core_network.user_posts.LogInRequest
import com.example.core_network.user_responses.LogInResponse
import retrofit2.Response
import javax.inject.Inject

class RequestMaker @Inject constructor(
    private val serviceToLogIn: LogInInterface,
    private val serviceForLocation: LocationInterface
) {
    suspend fun logInUser(logInRequest: LogInRequest): ResultOfRequest<LogInResponse> {
        return safeApiCall(call = { serviceToLogIn.logInUser(logInRequest) })
    }

    suspend fun getAllAvailableCountries(): ResultOfRequest<List<GetAllAvailableCountriesResponse>> {
        return safeApiCall(call = { serviceForLocation.getAllAvailableCountries() })
    }

    suspend fun getAllAvailableCountries(country: GetAllAvailableCitiesRequest): ResultOfRequest<List<GetAllAvailableCitiesResponse>> {
        return safeApiCall(call = { serviceForLocation.getAllAvailableCities(country) })
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