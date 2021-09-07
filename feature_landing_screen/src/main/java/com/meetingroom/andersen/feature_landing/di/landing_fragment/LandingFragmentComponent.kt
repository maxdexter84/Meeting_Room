package com.meetingroom.andersen.feature_landing.di.landing_fragment

import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meetingroom.andersen.feature_landing.di.Screen
import com.meetingroom.andersen.feature_landing.landing_fragment.ui.LandingFragment
import dagger.Component

@Component(modules = [LandingFragmentModule::class, SharedPreferencesModule::class])
@Screen
interface LandingFragmentComponent {
    fun inject(landingFragment: LandingFragment)
}