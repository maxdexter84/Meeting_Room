package com.meetingroom.feature_login

import androidx.lifecycle.ViewModel
import com.example.core_network.DaggerNetworkComponent
import com.example.core_network.user_interfaces.LogInInterface
import com.example.core_network.user_posts.LogInRequest
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit

class LoginFragmentViewModel : ViewModel() {
    fun tryToLogIn(text: String?, text1: String?): Boolean {
        val b = retrofit.create(LogInInterface::class.java)
        var a = runBlocking { b.logInUser(LogInRequest(text!!, text1!!)) }
        if (a.isSuccessful){

            return true
        }
        return false
    }

    private val retrofit: Retrofit = DaggerNetworkComponent.create().retrofit()

}