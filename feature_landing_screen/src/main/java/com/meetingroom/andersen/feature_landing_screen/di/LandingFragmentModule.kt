package com.meetingroom.andersen.feature_landing_screen.di

import androidx.lifecycle.ViewModelProvider
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import com.example.core_module.user_logout.LogOutHelper
import dagger.Module
import dagger.Provides
import com.meetingroom.andersen.feature_landing_screen.landing_fragment.LandingFragment
import com.meetingroom.andersen.feature_landing_screen.landing_fragment.LandingFragmentViewModel
import com.meetingroom.andersen.feature_landing_screen.landing_fragment.LandingFragmentViewModelFactory

@Module
class LandingFragmentModule(private val landingFragment: LandingFragment) {
    @Provides
    @Screen
    fun provideLogOutHelper(saveData: UserDataPrefHelperImpl): LogOutHelper {
        return LogOutHelper(saveData)
    }

    @Provides
    @Screen
    fun provideViewModelFactory(logOutHelper: LogOutHelper): LandingFragmentViewModelFactory =
        LandingFragmentViewModelFactory(logOutHelper)

    @Provides
    @Screen
    fun provideViewModel(landingFragmentViewModelFactory: LandingFragmentViewModelFactory): LandingFragmentViewModel {
        return ViewModelProvider(
            landingFragment,
            landingFragmentViewModelFactory
        ).get(LandingFragmentViewModel::class.java)
    }

}