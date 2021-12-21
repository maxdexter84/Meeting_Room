package com.meetingroom.feature_login.data

import com.example.core_network.RequestMaker
import com.example.core_network.RequestResult
import com.meetingroom.feature_login.domain.ILoginRepository
import com.meetingroom.feature_login.domain.LoginRequest
import com.meetingroom.feature_login.domain.LoginResponseDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val requestMaker: RequestMaker,
    private val authApi: AuthApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ILoginRepository {

    override suspend fun authenticateUser(loginRequest: LoginRequest): RequestResult<LoginResponseDto> = withContext(ioDispatcher) {
        requestMaker.safeApiCall{
            authApi.authenticateUser(loginRequest)
        }
    }

    override suspend fun refreshToken(refToken: String): RequestResult<LoginResponseDto> = withContext(ioDispatcher) {
        requestMaker.safeApiCall{
            authApi.refreshToken("$BEARER_FOR_TOKEN $refToken")
        }
    }

    companion object {
        const val BEARER_FOR_TOKEN = "Bearer"
    }
}
