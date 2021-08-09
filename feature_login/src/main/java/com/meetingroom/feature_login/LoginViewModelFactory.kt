package com.meetingroom.feature_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core_module.sharedpreferences.save_data.SaveData

class LoginViewModelFactory(val saveNetworkData: SaveData) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        LoginFragmentViewModel(saveNetworkData) as T
}