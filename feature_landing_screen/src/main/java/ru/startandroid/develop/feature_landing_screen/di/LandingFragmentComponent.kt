package ru.startandroid.develop.feature_landing_screen.di

import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import dagger.Component
import ru.startandroid.develop.feature_landing_screen.landing_fragment.LandingFragment

@Component(modules = [LandingFragmentModule::class, SharedPreferencesModule::class])
@Screen
interface LandingFragmentComponent {
    fun inject(landingFragment: LandingFragment)
}