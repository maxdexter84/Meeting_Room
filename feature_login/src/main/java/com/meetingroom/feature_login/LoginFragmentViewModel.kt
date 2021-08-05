package com.meetingroom.feature_login

import androidx.lifecycle.ViewModel
import com.example.core_network.user_interfaces.LogInInterface
import com.example.core_network.user_posts.LogInRequest
import com.example.core_network.user_responses.LogInResponse
import com.example.sharedpreferences.sharedpreferences.save_data.SaveNetworkData
import kotlinx.coroutines.*
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class LoginFragmentViewModel @Inject constructor(
    val retrofit: Retrofit,
    val saveNetworkData: SaveNetworkData
) : ViewModel() {


    fun tryToLogIn(login: String, password: String): Boolean {
        var result: Boolean = false
        val logIn = retrofit.create(LogInInterface::class.java)
        if (!isInputValid(login, password)) {
            return false
        }

        CoroutineScope(Dispatchers.IO).launch {
            async {
                val request: Response<LogInResponse> =
                    logIn.logInUser(LogInRequest(login, password))
                result = request.isSuccessful
                if (result) {
//                saveNetworkData.saveToken(request.body()!!.accessToken)
                }

                return@async true
            }



        }

        return result
    }

    private fun isInputValid(login: String, password: String): Boolean {
        //TODO add normal check
        if (login.isEmpty() || password.isEmpty()) return false
        //  if (!login.contentEquals("@") || !login.contentEquals(".")) return false

        return true
    }


}