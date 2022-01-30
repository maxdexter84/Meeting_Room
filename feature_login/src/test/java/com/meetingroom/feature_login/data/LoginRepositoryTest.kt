package com.meetingroom.feature_login.data

import com.example.core_network.RequestResult
import com.meetingroom.feature_login.domain.LoginRequest
import com.meetingroom.feature_login.domain.LoginResponseDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
const val REFRESH_TOKEN = "refreshToken"
const val TOKEN = "token"
class LoginRepositoryTest {

    private val repository = mock(LoginRepository::class.java)
    private val loginRequest = mock(LoginRequest::class.java)

    @Test
    fun authenticateUserReturnRequestResult() {
        Mockito.`when`(runBlocking {
            repository.authenticateUser(loginRequest)
        }).thenReturn(RequestResult.Success(LoginResponseDto(REFRESH_TOKEN, TOKEN)))

        val actual = runBlocking {
            repository.authenticateUser(loginRequest)
        }
        val expected = RequestResult.Success(LoginResponseDto(REFRESH_TOKEN, TOKEN))
        assertEquals(expected, actual)

    }

    @Test
    fun refreshTokenReturnRequestResult() {
        Mockito.`when`(runBlocking { repository.refreshToken(REFRESH_TOKEN) })
            .thenReturn(RequestResult.Success(LoginResponseDto(REFRESH_TOKEN, TOKEN)))
        val actual = runBlocking {
            repository.refreshToken(REFRESH_TOKEN)
        }
        val expected = RequestResult.Success(LoginResponseDto(REFRESH_TOKEN, TOKEN))
        assertEquals(expected, actual)
    }
}