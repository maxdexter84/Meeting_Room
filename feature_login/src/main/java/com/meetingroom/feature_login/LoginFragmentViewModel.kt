package com.meetingroom.feature_login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_network.ApiHelper
import com.example.core_network.ResultOfRequest
import com.example.core_network.user_posts.LogInRequest
import com.example.sharedpreferences.sharedpreferences.save_data.SaveNetworkData
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginFragmentViewModel @Inject constructor(
    private val saveNetworkData: SaveNetworkData
) : ViewModel() {
    val requestResult: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val errorMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun tryToLogIn(login: String, password: String) {
        if (!isInputValid(login, password)) {
            errorMessage.postValue(saveNetworkData.getContext().resources.getString(R.string.error_for_log_in))
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
                    errorMessage.postValue(
                        saveNetworkData.getContext().resources.getString(R.string.error_for_log_in)
                    )
                }
            }
        }
    }

    private fun isInputValid(login: String, password: String): Boolean {
        //TODO add normal check
        if (login.isEmpty() || password.isEmpty()) return false
        //  if (!login.contentEquals("@") || !login.contentEquals(".")) return false

        return true
    }

}