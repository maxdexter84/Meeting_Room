package com.meetingroom.feature_login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import com.example.core_network.RequestMaker
import com.example.core_network.ResultOfRequest
import com.example.core_network.user_posts.LogInRequest
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginFragmentViewModel @Inject constructor(
    private val saveNetworkData: UserDataPrefHelperImpl,
    private val requestMaker: RequestMaker
) : ViewModel() {
    val requestResult: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val errorMessage: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    fun tryToLogIn(login: String, password: String) {
        if (!isInputValid(login, password)) {
            errorMessage.postValue(R.string.error_for_log_in)
            return
        }
        viewModelScope.launch {
            when (val retrofitPost =
                requestMaker.logInUser(LogInRequest(login, password))) {
                is ResultOfRequest.Success -> {
                    requestResult.postValue(retrofitPost.data.email)
                    saveNetworkData.saveToken(retrofitPost.data.accessToken)
                    saveNetworkData.saveUserRoles(retrofitPost.data.roles)
                }
                is ResultOfRequest.Error -> {
                    errorMessage.postValue(R.string.error_for_log_in)
                }
            }
        }
    }

    private fun isInputValid(login: String, password: String): Boolean {
        return login.length > 7 && password.length > 7 && login.contains("@andersenlab.com")
                && Regex("(?=.*[0-9])(?=.*[!@?#\$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#\$%^&*]{8,}")
            .find(
                password
            ) != null
    }

}