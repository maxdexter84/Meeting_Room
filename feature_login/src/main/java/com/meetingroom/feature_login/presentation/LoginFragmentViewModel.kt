package com.meetingroom.feature_login.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_module.sharedpreferences.SharedPreferencesKeys
import com.example.core_network.RequestResult
import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper
import com.meetingroom.feature_login.domain.ILoginRepository
import com.meetingroom.feature_login.domain.LoginRequest
import com.meetingroom.feature_login.domain.LoginResponseDto
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class LoginFragmentViewModel @Inject constructor(
    private val loginRepository: ILoginRepository,
    private val saveNetworkData: UserDataPrefHelper
) : ViewModel() {

    val loginRequestResult: MutableLiveData<RequestResult<LoginResponseDto>> by lazy {
        MutableLiveData()
    }

    fun tryToLogIn(email: String, password: String) {
        loginRequestResult.postValue(RequestResult.Loading)
        val accessToken = saveNetworkData.getToken(SharedPreferencesKeys.ACCESS_TOKEN_KEY)
        checkValidEmailAndPassword(email, password)
        if(accessToken.isNullOrEmpty()){
            authenticateUser(email, password)
        } else{
            checkAccessTokenForUser(email, password)
        }
    }

    private fun checkValidEmailAndPassword(email: String, password: String){
        if (!isInputValid(email, password)) {
            loginRequestResult.postValue(RequestResult.Error(EXCEPTION_INCORRECT_EMAIL_OR_PASSWORD, CODE_UNAUTHORIZED))
        }
    }

    private fun authenticateUser(email: String, password: String){
        viewModelScope.launch {
            val response = loginRepository.authenticateUser(LoginRequest(email, password))
            checkResponse(response)
        }
    }

    private fun checkAccessTokenForUser(email: String, password: String){
        val refreshTime = saveNetworkData.getTokenDay()
        val currentTime: Long = Calendar.getInstance().timeInMillis
        val limitTime = LIMIT_TIME_FOR_ACCESS_TOKEN_FOR_USER - BUFFER_TIME_FOR_REFRESH_ACCESS_TOKEN
        if(refreshTime != null) {
            val elapsedTime = currentTime - refreshTime
            if (elapsedTime < limitTime) {
                authenticateUser(email, password)
            } else {
                viewModelScope.launch {
                    saveNetworkData.deleteToken()
                    val refToken = saveNetworkData.getToken(SharedPreferencesKeys.REFRESH_TOKEN_KEY).toString()
                    val response = loginRepository.refreshToken(refToken)
                    checkResponse(response)
                }
            }
        }
    }

    private fun checkResponse(response: RequestResult<LoginResponseDto>){
        when(response){
            is RequestResult.Success -> {
                saveNetworkData.token = response.data.token
                saveNetworkData.refreshToken = response.data.refreshToken
                loginRequestResult.value = response
            }
            is RequestResult.Error -> loginRequestResult.postValue(RequestResult.Error(response.exception, response.code))
            is RequestResult.Loading -> loginRequestResult.value = response
        }
    }

    private fun isInputValid(login: String, password: String): Boolean {
        return password.length >= MIN_PASSWORD_LENGTH
                && Regex(LOGIN_REGEX).matches(login)
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 4
        private const val LOGIN_REGEX = ".*@.+[.].{2,}"
        private const val EXCEPTION_INCORRECT_EMAIL_OR_PASSWORD = "Incorrect email or password"
        private const val CODE_UNAUTHORIZED = 401
        private const val LIMIT_TIME_FOR_ACCESS_TOKEN_FOR_USER = 604800000
        private const val BUFFER_TIME_FOR_REFRESH_ACCESS_TOKEN = 10000
    }
}
