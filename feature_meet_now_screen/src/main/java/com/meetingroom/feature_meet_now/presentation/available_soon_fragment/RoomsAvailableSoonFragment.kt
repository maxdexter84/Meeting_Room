package com.meetingroom.feature_meet_now.presentation.available_soon_fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core_module.component_manager.XInjectionManager
import com.example.core_module.state.State
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view_utils.visibilityIf
import com.meetingroom.feature_meet_now.domain.entity.Room
import com.meetingroom.feature_meet_now.presentation.available_now_fragment.RoomsAvailableSoonAdapter
import com.meetingroom.feature_meet_now.presentation.di.MeetNowComponent
import com.meetingroom.feature_meet_now.presentation.utils.RefreshTimer
import com.meetingroom.feature_meet_now.presentation.viewmodel.MeetNowSharedViewModel
import com.meetingroom.feature_meet_now_screen.databinding.FragmentAvailableSoonBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomsAvailableSoonFragment : BaseFragment<FragmentAvailableSoonBinding>(
    FragmentAvailableSoonBinding::inflate) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MeetNowSharedViewModel by viewModels {
        viewModelFactory
    }

    private val roomsAdapter by lazy {
        RoomsAvailableSoonAdapter(mutableListOf()) {
            bookRoom(it)
        }
    }

    private var refreshTimer = RefreshTimer(lifecycleScope)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.findComponent<MeetNowComponent>().inject(this)
        viewModel.getRoomsAvailableSoon()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRefreshLayout()
        initRecyclerView()
        loadingStateObserver()
        roomsAvailableSoonObserver()
    }

    override fun onStop() {
        super.onStop()
        refreshTimer.stop()
    }

    private fun initRefreshLayout() {
        with(binding.roomsAvailableSoonSwipeContainer) {
            setOnRefreshListener {
                viewModel.getRoomsAvailableSoon()
            }
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            roomsAvailableSoonRecyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = roomsAdapter
            }
        }
    }

    private fun roomsAvailableSoonObserver() {
        lifecycleScope.launch {
            viewModel.roomsAvailableSoon.collectLatest { list ->
                roomsAdapter.setData(list.sortedBy { it.availableIn })
            }
        }
    }

    private fun loadingStateObserver() {
        with(binding) {
            lifecycleScope.launch {
                viewModel.loadingState.collectLatest {
                    when (it) {
                        is State.Loading -> {
                            roomsAvailableSoonRecyclerView.isVisible = false
                            roomsAvailableSoonProgressBar.isVisible = true
                            roomsAvailableSoonSwipeContainer.isRefreshing = false
                            displayNoRoomsAvailableSoonMessage(false)
                        }
                        is State.NotLoading -> {
                            roomsAvailableSoonRecyclerView.isVisible = true
                            roomsAvailableSoonProgressBar.isVisible = false
                            refreshTimer.start { viewModel.getRoomsAvailableSoon() }
                            if (viewModel.roomsAvailableSoon.value.isEmpty()) {
                                displayNoRoomsAvailableSoonMessage(true)
                            }
                        }
                        is State.Error -> {
                            roomsAvailableSoonRecyclerView.isVisible = false
                            roomsAvailableSoonProgressBar.isVisible = false
                        }
                    }
                }
            }
        }
    }

    private fun displayNoRoomsAvailableSoonMessage(isVisible: Boolean) {
        with(binding) {
            noRoomsAvailableSoonGroup.visibilityIf(isVisible)
        }
    }

    private fun bookRoom(room: Room) {
        // TODO: Display Fast Booking Slider screen
    }
}
