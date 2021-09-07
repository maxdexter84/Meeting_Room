package com.meetingroom.andersen.feature_landing.history_of_events_fragment.ui

import android.content.Context
import com.meeringroom.ui.view.base_fragment.BaseFragment
import com.meetingroom.andersen.feature_landing.databinding.FragmentHistoryOfEventsBinding
import com.meetingroom.andersen.feature_landing.di.history_of_events_fragment.DaggerHistoryOfEventsFragmentComponent
import com.meetingroom.andersen.feature_landing.history_of_events_fragment.presentation.HistoryOfEventsFragmentViewModel
import javax.inject.Inject

class HistoryOfEventsFragment :
    BaseFragment<FragmentHistoryOfEventsBinding>(FragmentHistoryOfEventsBinding::inflate) {

    @Inject
    lateinit var viewModel: HistoryOfEventsFragmentViewModel

    override fun onAttach(context: Context) {
        DaggerHistoryOfEventsFragmentComponent.builder()
            .build()
            .inject(this)
        super.onAttach(context)
    }
}