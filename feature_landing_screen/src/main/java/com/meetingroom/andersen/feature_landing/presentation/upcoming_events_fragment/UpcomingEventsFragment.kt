package com.meetingroom.andersen.feature_landing.presentation.upcoming_events_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.core_module.component_manager.XInjectionManager
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view_utils.visibilityIf
import com.meetingroom.andersen.feature_landing.databinding.FragmentUpcomingEventsBinding
import com.meetingroom.andersen.feature_landing.domain.entity.UpcomingEventData
import com.meetingroom.andersen.feature_landing.presentation.di.LandingComponent
import com.meetingroom.andersen.feature_landing.presentation.landing_fragment.LandingFragmentDirections
import javax.inject.Inject

class UpcomingEventsFragment :
    BaseFragment<FragmentUpcomingEventsBinding>(FragmentUpcomingEventsBinding::inflate) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: UpcomingEventsFragmentViewModel by viewModels {
        viewModelFactory
    }

    private val eventAdapter = UpcomingEventAdapter(onEventClicked = {
        navigateToModify(it)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.findComponent<LandingComponent>().inject(this)
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