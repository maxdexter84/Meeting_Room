package com.meetingroom.andersen.feature_landing.presentation.upcoming_events_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.core_module.component_manager.XInjectionManager
import com.example.core_module.state.State
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view_utils.visibilityIf
import com.meetingroom.andersen.feature_landing.databinding.FragmentUpcomingEventsBinding
import com.meetingroom.andersen.feature_landing.di.upcoming_events_fragment.DaggerUpcomingEventsFragmentComponent
import com.meetingroom.andersen.feature_landing.di.upcoming_events_fragment.UpcomingEventsFragmentModule
import com.meetingroom.andersen.feature_landing.landing_fragment.ui.LandingFragmentDirections
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.model.UpcomingEventData
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.presentation.UpcomingEventsFragmentViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
        upcomingEventsListObserver()
    }

    private fun upcomingEventsListObserver(){
        lifecycleScope.launch {
            viewModel.upcomingEvents.collectLatest {
                eventAdapter.setData(it)
                loadingStateObserver()
                initEmptyUpcomingMessage(it.isEmpty())
            }
        }
    }

    private fun loadingStateObserver() {
        lifecycleScope.launch {
            viewModel.mutableState.collectLatest {
                when (it) {
                    is State.Loading -> {
                        with(binding) {
                            progressBarUpcomingEvents.isVisible = true
                            initEmptyUpcomingMessage(false)
                        }
                    }
                    else -> binding.progressBarUpcomingEvents.isVisible = false
                }
            }
        }
    }

    private fun initEmptyUpcomingMessage(visibility: Boolean) {
        with(binding) {
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