package com.meetingroom.feature_login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper
import com.meetingroom.feature_login.domain.ILoginRepository

class LoginViewModelFactory(
    private val loginRepository: ILoginRepository,
    private val saveNetworkData: UserDataPrefHelper
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        LoginFragmentViewModel(loginRepository, saveNetworkData) as T
}