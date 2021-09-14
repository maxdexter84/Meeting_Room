package com.meetingroom.andersen.feature_landing.di.landing_fragment

import androidx.lifecycle.ViewModelProvider
import com.example.core_module.sharedpreferences.save_data.UserDataPrefHelperImpl
import com.example.core_module.user_logout.LogOutHelper
import com.meetingroom.andersen.feature_landing.di.Screen
import com.meetingroom.andersen.feature_landing.landing_fragment.presentation.LandingFragmentViewModel
import com.meetingroom.andersen.feature_landing.landing_fragment.presentation.LandingFragmentViewModelFactory
import com.meetingroom.andersen.feature_landing.landing_fragment.ui.LandingFragment
import dagger.Module
import dagger.Provides

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