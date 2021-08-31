package com.meetingroom.andersen.feature_landing.di

import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import dagger.Component
import com.meetingroom.andersen.feature_landing.landing_fragment.LandingFragment

@Component(modules = [LandingFragmentModule::class, SharedPreferencesModule::class])
@Screen
interface LandingFragmentComponent {
    fun inject(landingFragment: LandingFragment)
}