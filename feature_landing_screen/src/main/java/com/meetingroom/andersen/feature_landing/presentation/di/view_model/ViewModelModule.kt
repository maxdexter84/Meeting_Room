package com.meetingroom.andersen.feature_landing.presentation.di.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meetingroom.andersen.feature_landing.presentation.history_of_events_fragment.HistoryOfEventsFragmentViewModel
import com.meetingroom.andersen.feature_landing.presentation.landing_fragment.LandingFragmentViewModel
import com.meetingroom.andersen.feature_landing.presentation.modify_upcoming_fragment.ModifyUpcomingEventViewModel
import com.meetingroom.andersen.feature_landing.presentation.upcoming_events_fragment.UpcomingEventsFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LandingFragmentViewModel::class)
    fun bindLandingFragmentViewModel(landingFragmentViewModel: LandingFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ModifyUpcomingEventViewModel::class)
    fun bindModifyUpcomingEventViewModel(modifyUpcomingEventViewModel: ModifyUpcomingEventViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UpcomingEventsFragmentViewModel::class)
    fun bindUpcomingEventsFragmentViewModel(upcomingEventsFragmentViewModel: UpcomingEventsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HistoryOfEventsFragmentViewModel::class)
    fun bindHistoryOfEventsFragmentViewModel(historyOfEventsFragmentViewModel: HistoryOfEventsFragmentViewModel): ViewModel
}
