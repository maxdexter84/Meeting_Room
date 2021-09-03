package com.meetingroom.andersen.feature_landing.upcoming_events_fragment.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core_module.user_logout.LogOutHelper
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.model.UpcomingEventData

class UpcomingEventsFragmentViewModel(
    private val logOutHelper: LogOutHelper,
    gagForUpcomingEvents: GagForUpcomingEvents
) : ViewModel() {

    private val mutableGagData = MutableLiveData<List<UpcomingEventData>>()
    val gagData: LiveData<List<UpcomingEventData>>
        get() = mutableGagData

    init {
        if (logOutHelper.isDeleteRequired()) logOutHelper.logout()
        mutableGagData.value = gagForUpcomingEvents.generate(9)
    }

    fun logout() {
        logOutHelper.logout()
    }
}