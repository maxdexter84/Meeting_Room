package com.meetingroom.andersen.feature_landing.upcoming_events_fragment.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view_utils.visibilityIf
import com.meetingroom.andersen.feature_landing.databinding.FragmentUpcomingEventsBinding
import com.meetingroom.andersen.feature_landing.di.upcoming_events_fragment.DaggerUpcomingEventsFragmentComponent
import com.meetingroom.andersen.feature_landing.di.upcoming_events_fragment.UpcomingEventsFragmentModule
import com.meetingroom.andersen.feature_landing.landing_fragment.ui.LandingFragmentDirections
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.model.UpcomingEventData
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.presentation.UpcomingEventsFragmentViewModel
import javax.inject.Inject

class UpcomingEventsFragment :
    BaseFragment<FragmentUpcomingEventsBinding>(FragmentUpcomingEventsBinding::inflate) {

    @Inject
    lateinit var viewModel: UpcomingEventsFragmentViewModel

    private val eventAdapter = UpcomingEventAdapter(onEventClicked = {
        navigateToModify(it)
    })

    override fun onAttach(context: Context) {
        DaggerUpcomingEventsFragmentComponent.builder()
            .upcomingEventsFragmentModule(UpcomingEventsFragmentModule(this))
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

    private fun initEmptyUpcomingMessage(visibility: Boolean) {
        with(binding) {
            progressBarUpcomingEvents.visibilityIf(false)
            emojiEmptyUpcomings.visibilityIf(visibility)
            feelsLonelyEmptyUpcomings.visibilityIf(visibility)
            bookMeetingSuggestionEmptyUpcomings.visibilityIf(visibility)
            upcomingEventsRecyclerView.visibilityIf(!visibility)
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            upcomingEventsRecyclerView.apply {
                setHasFixedSize(true)
                adapter = eventAdapter
            }
        }
    }

    private fun navigateToModify(upcomingEventData: UpcomingEventData) {
        findNavController().navigate(
            LandingFragmentDirections.actionLandingFragmentToModifyUpcomingEventFragment(
                upcomingEventData,
            )
        )
    }
}