package com.meetingroom.feature_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sharedpreferences.sharedpreferences.save_data.SaveNetworkData
import retrofit2.Retrofit

class LoginViewModelFactory(val retrofit: Retrofit, val saveNetworkData: SaveNetworkData): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
       LoginFragmentViewModel(retrofit, saveNetworkData) as T
}