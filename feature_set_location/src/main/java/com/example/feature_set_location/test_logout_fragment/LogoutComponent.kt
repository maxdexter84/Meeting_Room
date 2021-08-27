package com.example.feature_set_location.test_logout_fragment

import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.example.feature_set_location.di.Screen
import dagger.Component

@Component(modules = [LogoutModule::class, SharedPreferencesModule::class])
@Screen
interface LogoutComponent {
    fun inject(logoutTestFragment: LogoutTestFragment)
}