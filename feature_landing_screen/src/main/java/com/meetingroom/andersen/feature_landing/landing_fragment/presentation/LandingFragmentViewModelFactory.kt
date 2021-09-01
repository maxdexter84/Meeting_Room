package com.meetingroom.andersen.feature_landing.landing_fragment.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core_module.user_logout.LogOutHelper

@Suppress("UNCHECKED_CAST")
class LandingFragmentViewModelFactory(private val logOutHelper: LogOutHelper) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LandingFragmentViewModel::class.java)) {
            return LandingFragmentViewModel(
                logOutHelper
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}