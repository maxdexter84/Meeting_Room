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
import com.meetingroom.feature_meet_now_screen.databinding.FragmentAvailableNowBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val REFRESH_TIME: Long = 60000

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

    private var refreshTimerJob: Job? = null

    private fun startRefreshTimer() {
        stopRefreshTimer()
        refreshTimerJob = lifecycleScope.launch {
            while (true) {
                delay(REFRESH_TIME)
                viewModel.getRoomsAvailableNow()
            }
        }
    }

    private fun stopRefreshTimer() {
        refreshTimerJob?.cancel()
        refreshTimerJob = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.findComponent<MeetNowComponent>().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRefreshLayout()
        initRecyclerView()
        loadingStateObserver()
        roomsAvailableNowObserver()
        viewModel.getRoomsAvailableNow()
    }

    override fun onStop() {
        super.onStop()
        stopRefreshTimer()
    }

    private fun initRefreshLayout() {
        with(binding.swipeContainer) {
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
                roomsAdapter.setData(list)
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
                            swipeContainer.isRefreshing = false
                            displayNoAvailableRoomsMessage(false)
                        }
                        is State.NotLoading -> {
                            roomsAvailableNowRecyclerView.isVisible = true
                            roomsAvailableNowProgressBar.isVisible = false
                            startRefreshTimer()
                            if (viewModel.roomsAvailableNow.value.isEmpty()) {
                                displayNoAvailableRoomsMessage(true)
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

    private fun displayNoAvailableRoomsMessage(isVisible: Boolean) {
        with(binding) {
            noRoomsEmoji.visibilityIf(isVisible)
            noRoomsTitle.visibilityIf(isVisible)
            noRoomsSubtitle.visibilityIf(isVisible)
        }
    }

    private fun bookRoom(room: Room) {
        // TODO: Display Fast Booking Slider screen
    }
}
