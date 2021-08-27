package com.example.feature_set_location.test_logout_fragment

import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import com.example.core_module.user_logout.LogoutUser
import dagger.Module
import dagger.Provides

@Module
class LogoutModule {

    @Provides
    fun provideLogoutUser(saveData: UserDataPrefHelperImpl): LogoutUser {
        return LogoutUser(saveData)
    }
}