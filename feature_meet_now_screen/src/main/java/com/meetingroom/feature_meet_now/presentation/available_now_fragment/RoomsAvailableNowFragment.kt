package com.meetingroom.feature_meet_now.presentation.available_now_fragment

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
import com.meetingroom.feature_meet_now.presentation.di.MeetNowComponent
import com.meetingroom.feature_meet_now.presentation.utils.RefreshTimer
import com.meetingroom.feature_meet_now_screen.databinding.FragmentAvailableNowBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomsAvailableNowFragment :
    BaseFragment<FragmentAvailableNowBinding>(FragmentAvailableNowBinding::inflate) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: RoomsAvailableNowViewModel by viewModels {
        viewModelFactory
    }

    private val roomsAdapter by lazy {
        RoomsAvailableNowAdapter(mutableListOf()) {
            bookRoom(it)
        }
    }

    val refreshTimer = RefreshTimer(lifecycleScope)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.findComponent<MeetNowComponent>().inject(this)
        viewModel.getRoomsAvailableNow()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRefreshLayout()
        initRecyclerView()
        loadingStateObserver()
        roomsAvailableNowObserver()
    }

    override fun onStop() {
        super.onStop()
        refreshTimer.stop()
    }

    private fun initRefreshLayout() {
        with(binding.roomsAvailableNowSwipeContainer) {
            setOnRefreshListener {
                viewModel.getRoomsAvailableNow()
            }
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            roomsAvailableNowRecyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = roomsAdapter
            }
        }
    }

    private fun roomsAvailableNowObserver() {
        lifecycleScope.launch {
            viewModel.roomsAvailableNow.collectLatest { list ->
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
                            roomsAvailableNowRecyclerView.isVisible = false
                            roomsAvailableNowProgressBar.isVisible = true
                            roomsAvailableNowSwipeContainer.isRefreshing = false
                            displayNoRoomsAvailableNowMessage(false)
                        }
                        is State.NotLoading -> {
                            roomsAvailableNowRecyclerView.isVisible = true
                            roomsAvailableNowProgressBar.isVisible = false
                            refreshTimer.start { viewModel.getRoomsAvailableNow() }
                            if (viewModel.roomsAvailableNow.value.isEmpty()) {
                                displayNoRoomsAvailableNowMessage(true)
                            }
                        }
                        is State.Error -> {
                            roomsAvailableNowRecyclerView.isVisible = false
                            roomsAvailableNowProgressBar.isVisible = false
                        }
                    }
                }
            }
        }
    }

    private fun displayNoRoomsAvailableNowMessage(isVisible: Boolean) {
        with(binding) {
            noAvailableRoomsForNowPlaceholder.root.visibilityIf(isVisible)
        }
    }

    private fun bookRoom(room: Room) {
        // TODO: Display Fast Booking Slider screen
    }
}
