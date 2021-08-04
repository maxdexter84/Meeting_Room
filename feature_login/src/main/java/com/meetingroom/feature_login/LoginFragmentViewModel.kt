package com.meetingroom.feature_login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.core_network.DaggerNetworkComponent
import com.example.core_network.user_interfaces.LogInInterface
import com.example.core_network.user_posts.LogInRequest
import com.example.sharedpreferences.DaggerSharedPreferencesComponent
import com.example.sharedpreferences.SharedPreferencesModule
import com.example.sharedpreferences.sharedpreferences.SharedPreferencesHelper
import com.example.sharedpreferences.sharedpreferences.save_data.SaveNetworkData
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit

class LoginFragmentViewModel : ViewModel() {

    private val retrofit: Retrofit = DaggerNetworkComponent.create().retrofit()
    lateinit var context: Context
    private lateinit var saveNetworkData: SaveNetworkData

    fun tryToLogIn(login: String, password: String): Boolean {
        val logIn = retrofit.create(LogInInterface::class.java)
        if (!isInputValid(login, password)) {
            return false
        }
        val request = runBlocking { logIn.logInUser(LogInRequest(login, password)) }
        if (request.isSuccessful) {
            saveNetworkData = DaggerSharedPreferencesComponent.builder().sharedPreferencesModule(
                SharedPreferencesModule(context)
            ).build().saveNetworkData()
            saveNetworkData = SaveNetworkData(SharedPreferencesHelper(context))
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