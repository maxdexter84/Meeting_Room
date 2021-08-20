package com.meetingroom.feature_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import com.example.core_network.RequestMaker

class LoginViewModelFactory(val data: UserDataPrefHelperImpl, val requestMaker: RequestMaker) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        LoginFragmentViewModel(data, requestMaker) as T
}