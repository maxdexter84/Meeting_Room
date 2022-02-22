package com.meetingroom.feature_meet_now.presentation.available_soon_fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core_module.component_manager.XInjectionManager
import com.example.core_module.state.State
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meetingroom.feature_meet_now.domain.entity.Room
import com.meetingroom.feature_meet_now.presentation.meet_now_fragment.MeetNowFragment
import com.meetingroom.feature_meet_now.presentation.meet_now_fragment.MeetNowFragmentDirections
import com.meetingroom.feature_meet_now.presentation.utils.RefreshTimer
import com.meetingroom.feature_meet_now_screen.databinding.FragmentAvailableSoonBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomsAvailableSoonFragment : BaseFragment<FragmentAvailableSoonBinding>(
    FragmentAvailableSoonBinding::inflate) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: RoomsAvailableSoonViewModel by viewModels {
        viewModelFactory
    }

    private val roomsAvailableSoonAdapter by lazy {
        RoomsAvailableSoonAdapter(mutableListOf()) {
            bookRoom(it)
        }
    }

    private val roomsAvailableLaterAdapter by lazy {
        RoomsAvailableLaterAdapter(mutableListOf()) {
            bookRoom(it)
        }
    }

    private var refreshTimer = RefreshTimer(lifecycleScope)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.bindComponent(this.parentFragment as MeetNowFragment).inject(this)
        viewModel.getRoomsAvailableSoon()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRefreshLayout()
        initRecyclerView()
        loadingStateObserver()
        availableRoomsObserver()
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
            }
        }
    }

    private fun availableRoomsObserver() {
        lifecycleScope.launch {
            viewModel.availableRooms.collectLatest { list ->
                when (viewModel.roomsFound) {
                    RoomsFound.ROOMS_AVAILABLE_SOON -> {
                        binding.roomsAvailableSoonRecyclerView.adapter = roomsAvailableSoonAdapter
                        roomsAvailableSoonAdapter.setData(list.sortedBy { it.availableIn })
                    }
                    RoomsFound.ROOMS_AVAILABLE_LATER -> {
                        binding.roomsAvailableSoonRecyclerView.adapter = roomsAvailableLaterAdapter
                        roomsAvailableLaterAdapter.setData(list.sortedBy { it.availableIn })
                    }
                    RoomsFound.NO_ROOMS -> {
                    }
                }
            }
        }
    }

    private fun loadingStateObserver() {
        lifecycleScope.launch {
            viewModel.loadingState.collectLatest {
                when (it) {
                    is State.Loading -> displayLoading()
                    is State.NotLoading -> {
                        refreshTimer.start { viewModel.getRoomsAvailableSoon() }
                        when (viewModel.roomsFound) {
                            RoomsFound.ROOMS_AVAILABLE_SOON,
                            RoomsFound.ROOMS_AVAILABLE_LATER -> {
                                displayRooms()
                            }
                            RoomsFound.NO_ROOMS -> {
                                displayNoRoomsAvailable()
                            }
                        }
                    }
                    is State.Error -> displayError()
                }
            }
        }
    }

    private fun displayRooms() {
        with(binding) {
            roomsAvailableSoonProgressBar.isVisible = false
            youDontNeedToWaitPlaceholder.root.isVisible = false
            roomsAvailableSoonRecyclerView.isVisible = true
        }
    }

    private fun displayNoRoomsAvailable() {
        with(binding) {
            roomsAvailableSoonProgressBar.isVisible = false
            roomsAvailableSoonRecyclerView.isVisible = false
            youDontNeedToWaitPlaceholder.root.isVisible = true
        }
    }

    private fun displayLoading() {
        with(binding) {
            roomsAvailableSoonSwipeContainer.isRefreshing = false
            youDontNeedToWaitPlaceholder.root.isVisible = false
            roomsAvailableSoonRecyclerView.isVisible = false
            roomsAvailableSoonProgressBar.isVisible = true
        }
    }

    private fun displayError() {
        with(binding) {
            youDontNeedToWaitPlaceholder.root.isVisible = false
            roomsAvailableSoonRecyclerView.isVisible = false
            roomsAvailableSoonProgressBar.isVisible = false
        }
    }

    private fun bookRoom(room: Room) {
        findNavController().navigate(
            MeetNowFragmentDirections.actionMeetNowFragmentToFastBookingFragment(
                room
            )
        )
    }
}
