package com.meetingroom.andersen.feature_landing.presentation.di

import android.content.Context
import com.example.core_module.di.FeatureScope
import com.meetingroom.andersen.feature_landing.presentation.di.view_model.ViewModelModule
import com.meetingroom.andersen.feature_landing.presentation.history_of_events_fragment.HistoryOfEventsFragment
import com.meetingroom.andersen.feature_landing.presentation.landing_fragment.LandingFragment
import com.meetingroom.andersen.feature_landing.presentation.modify_upcoming_fragment.ModifyUpcomingEventFragment
import com.meetingroom.andersen.feature_landing.presentation.upcoming_events_fragment.UpcomingEventsFragment
import dagger.BindsInstance
import dagger.Component

@Component(modules = [LandingNetworkModule::class,
    ModifyUpcomingEventFragmentModule::class,  ViewModelModule::class],  dependencies = [LandingDeps::class])
@FeatureScope
interface LandingComponent {

    fun inject(historyOfEventsFragment: HistoryOfEventsFragment)
    fun inject(historyOfEventsFragment: ModifyUpcomingEventFragment)
    fun inject(landingFragment: LandingFragment)
    fun inject(upcomingEventsFragment: UpcomingEventsFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context, landingDeps: LandingDeps): LandingComponent
    }
}