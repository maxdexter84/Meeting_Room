package com.meetingroom.feature_login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_network.login_stuff.LogInApiHelper
import com.example.core_network.login_stuff.ResultOfLogIn
import com.example.core_network.user_interfaces.LogInInterface
import com.example.core_network.user_posts.LogInRequest
import com.example.core_network.user_responses.LogInResponse
import com.example.sharedpreferences.sharedpreferences.save_data.SaveNetworkData
import kotlinx.coroutines.*
import retrofit2.Retrofit
import javax.inject.Inject

class LoginFragmentViewModel @Inject constructor(
    val retrofit: Retrofit,
    val saveNetworkData: SaveNetworkData
) : ViewModel() {
    val post: MutableLiveData<LogInResponse> by lazy {
        MutableLiveData<LogInResponse>()
    }

    val errorMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun tryToLogIn(login: String, password: String) {
        val logIn = retrofit.create(LogInInterface::class.java)
        if (!isInputValid(login, password)) {
            errorMessage.postValue("Incorrect e-mail or password!")
            return
        }
        viewModelScope.launch {
            when (val retrofitPost =
                LogInApiHelper(logIn).logInUser(LogInRequest(login, password))) {
                is ResultOfLogIn.Success -> {
                    post.postValue(retrofitPost.data)
                    saveNetworkData.saveToken(retrofitPost.data.accessToken)
                }
                is ResultOfLogIn.Error -> {
                    errorMessage.postValue("Incorrect e-mail or password!")
                }
            }
        }

//        CoroutineScope(Dispatchers.IO).launch {
//            async {
//                val request: Response<LogInResponse> =
//                    logIn.logInUser(LogInRequest(login, password))
//                result = request.isSuccessful
//                if (result) {
////                saveNetworkData.saveToken(request.body()!!.accessToken)
//                }
//
//                return@async true
//            }
//
//        }

    }

    private fun isInputValid(login: String, password: String): Boolean {
        //TODO add normal check
        if (login.isEmpty() || password.isEmpty()) return false
        //  if (!login.contentEquals("@") || !login.contentEquals(".")) return false

        return true
    }


}