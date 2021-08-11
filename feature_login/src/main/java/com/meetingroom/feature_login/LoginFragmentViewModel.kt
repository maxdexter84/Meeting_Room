package com.meetingroom.feature_login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import com.example.core_network.ApiHelper
import com.example.core_network.ResultOfRequest
import com.example.core_network.user_posts.LogInRequest
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginFragmentViewModel @Inject constructor(
    private val saveNetworkData: UserDataPrefHelperImpl
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
                ApiHelper.logInUser(LogInRequest(login, password))) {
                is ResultOfRequest.Success -> {
                    requestResult.postValue(retrofitPost.data.email)
                    saveNetworkData.saveToken(retrofitPost.data.accessToken)
                }
                is ResultOfRequest.Error -> {
                    errorMessage.postValue(R.string.error_for_log_in)
                }
            }
        }
    }

    private fun isInputValid(login: String, password: String): Boolean {
        if (login.isEmpty() || password.isEmpty()) return false
        if (login.length < 6 || password.length < 6) return false
        return true
    }

}