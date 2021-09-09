package com.meetingroom.andersen.feature_landing.history_of_events_fragment.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.meeringroom.ui.view.base_fragment.BaseFragment
import com.meeringroom.ui.view_utils.visibilityIf
import com.meetingroom.andersen.feature_landing.databinding.FragmentHistoryOfEventsBinding
import com.meetingroom.andersen.feature_landing.di.history_of_events_fragment.DaggerHistoryOfEventsFragmentComponent
import com.meetingroom.andersen.feature_landing.history_of_events_fragment.presentation.GagForHistoryEvents
import com.meetingroom.andersen.feature_landing.history_of_events_fragment.presentation.HistoryOfEventsFragmentViewModel
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.presentation.GagForUpcomingEvents
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.ui.UpcomingEventAdapter
import javax.inject.Inject

class HistoryOfEventsFragment :
    BaseFragment<FragmentHistoryOfEventsBinding>(FragmentHistoryOfEventsBinding::inflate) {

    private val eventAdapter by lazy { HistoryEventAdapter() }

    @Inject
    lateinit var viewModel: HistoryOfEventsFragmentViewModel

    override fun onAttach(context: Context) {
        DaggerHistoryOfEventsFragmentComponent.builder()
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        viewModel.gagData.observe(viewLifecycleOwner) {
            eventAdapter.setData(it)
            initEmptyUpcomingMessage(it.isEmpty())
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            historyEventsRecyclerView.apply {
                setHasFixedSize(true)
                adapter = eventAdapter
            }
        }
    }

    private fun initEmptyUpcomingMessage(visibility: Boolean) {
        with(binding) {
           progressBarHistoryEvents.visibility = View.GONE
           eventsBookedInTheLast10DaysTitle.visibilityIf(!visibility)
           historyEventsRecyclerView.visibilityIf(!visibility)
           noHistoryEventsMessage.visibilityIf(visibility)
        }
    }
}