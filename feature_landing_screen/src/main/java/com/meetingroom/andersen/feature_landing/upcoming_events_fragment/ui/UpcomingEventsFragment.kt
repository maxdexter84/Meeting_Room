package com.meetingroom.andersen.feature_landing.upcoming_events_fragment.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meeringroom.ui.view.base_fragment.BaseFragment
import com.meeringroom.ui.view_utils.visibilityIf
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.FragmentUpcomingEventsBinding
import com.meetingroom.andersen.feature_landing.di.upcoming_events_fragment.DaggerUpcomingEventsFragmentComponent
import com.meetingroom.andersen.feature_landing.di.upcoming_events_fragment.UpcomingEventsFragmentModule
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.model.UpcomingEventData
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.presentation.NotificationHelper
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.presentation.UpcomingEventsFragmentViewModel
import javax.inject.Inject

class UpcomingEventsFragment :
    BaseFragment<FragmentUpcomingEventsBinding>(FragmentUpcomingEventsBinding::inflate) {


    @Inject
    lateinit var viewModel: UpcomingEventsFragmentViewModel

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private val eventAdapter = UpcomingEventAdapter(onEventClicked = {
        navigateToModify()
    }, onBellClicked = { createNotification(it) })

    override fun onAttach(context: Context) {
        DaggerUpcomingEventsFragmentComponent.builder()
            .upcomingEventsFragmentModule(UpcomingEventsFragmentModule(this))
            .sharedPreferencesModule(SharedPreferencesModule(requireContext()))
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

    private fun navigateToModify() {
        findNavController().navigate(R.id.action_landingFragment_to_modifyUpcomingEventFragment)
    }

    private fun createNotification(upcomingEventData: UpcomingEventData) {
        NotificationHelper.setNotification(upcomingEventData, notificationHelper)
    }
}