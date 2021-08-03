package com.meetingroom.feature_login

import androidx.lifecycle.ViewModel
import com.example.core_network.DaggerNetworkComponent
import com.example.core_network.user_interfaces.LogInInterface
import com.example.core_network.user_posts.LogInRequest
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit

class LoginFragmentViewModel : ViewModel() {

    private val retrofit: Retrofit = DaggerNetworkComponent.create().retrofit()

    fun tryToLogIn(login: String, password: String): Boolean {
        val logIn = retrofit.create(LogInInterface::class.java)
        if (!isInputValid(login, password)) {
            return false
        }
        val request = runBlocking { logIn.logInUser(LogInRequest(login, password)) }
        if (request.isSuccessful) {
            // add sharedpreff
            return true
        }
        return false
    }

    private fun isInputValid(login: String, password: String): Boolean {
        //TODO add normal check
        if (login.isEmpty() || password.isEmpty()) return false
        //  if (!login.contentEquals("@") || !login.contentEquals(".")) return false

        return true
    }


}