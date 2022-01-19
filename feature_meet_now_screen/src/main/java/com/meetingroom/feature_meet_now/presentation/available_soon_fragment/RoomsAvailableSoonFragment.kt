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
import com.google.android.material.appbar.AppBarLayout
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meetingroom.feature_meet_now.domain.entity.Room
import com.meetingroom.feature_meet_now.presentation.di.MeetNowComponent
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
        XInjectionManager.findComponent<MeetNowComponent>().inject(this)
        viewModel.getRoomsAvailableSoon()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRefreshLayout()
        initRecyclerViews()
        loadingStateObserver()
        availableRoomsObserver()
    }

    override fun onStop() {
        super.onStop()
        refreshTimer.stop()
    }

    private fun initRefreshLayout() {
        with(binding) {
            roomsAvailableLaterAppbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                roomsAvailableSoonSwipeContainer.isEnabled = verticalOffset == 0
            })
            roomsAvailableSoonSwipeContainer.setOnRefreshListener {
                viewModel.getRoomsAvailableSoon()
            }
        }
    }

    private fun initRecyclerViews() {
        with(binding) {
            roomsAvailableSoonRecyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = roomsAvailableSoonAdapter
            }
            roomsAvailableLaterRecyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = roomsAvailableLaterAdapter
            }
        }
    }

    private fun availableRoomsObserver() {
        lifecycleScope.launch {
            viewModel.availableRooms.collectLatest { list ->
                when (viewModel.viewState) {
                    ViewState.ROOMS_AVAILABLE_SOON_FOUND -> roomsAvailableSoonAdapter.setData(list.sortedBy { it.availableIn })
                    ViewState.ROOMS_AVAILABLE_LATER_FOUND -> roomsAvailableLaterAdapter.setData(list.sortedBy { it.availableIn })
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
                        when (viewModel.viewState) {
                            ViewState.ROOMS_AVAILABLE_SOON_FOUND -> {
                                displayRoomsAvailableSoon()
                            }
                            ViewState.ROOMS_AVAILABLE_LATER_FOUND -> {
                                displayRoomsAvailableLater()
                            }
                            ViewState.NO_ROOMS_FOUND -> {
                                displayNoRoomsAvailable()
                            }
                        }
                    }
                    is State.Error -> displayError()
                }
            }
        }
    }

    private fun displayRoomsAvailableSoon() {
        with(binding) {
            roomsAvailableSoonProgressBar.isVisible = false
            roomsAvailableLaterAppbar.isVisible = false
            youDontNeedToWaitPlaceholder.root.isVisible = false
            roomsAvailableLaterRecyclerView.isVisible = false
            roomsAvailableSoonRecyclerView.isVisible = true
        }
    }

    private fun displayRoomsAvailableLater() {
        with(binding) {
            roomsAvailableSoonProgressBar.isVisible = false
            youDontNeedToWaitPlaceholder.root.isVisible = false
            roomsAvailableSoonRecyclerView.isVisible = false
            roomsAvailableLaterAppbar.isVisible = true
            roomsAvailableLaterRecyclerView.isVisible = true
        }
    }

    private fun displayNoRoomsAvailable() {
        with(binding) {
            roomsAvailableSoonProgressBar.isVisible = false
            roomsAvailableLaterAppbar.isVisible = false
            roomsAvailableSoonRecyclerView.isVisible = false
            roomsAvailableLaterRecyclerView.isVisible = false
            youDontNeedToWaitPlaceholder.root.isVisible = true
        }
    }

    private fun displayLoading() {
        with(binding) {
            roomsAvailableSoonSwipeContainer.isRefreshing = false
            roomsAvailableLaterAppbar.isVisible = false
            youDontNeedToWaitPlaceholder.root.isVisible = false
            roomsAvailableSoonRecyclerView.isVisible = false
            roomsAvailableLaterRecyclerView.isVisible = false
            roomsAvailableSoonProgressBar.isVisible = true
        }
    }

    private fun displayError() {
        with(binding) {
            roomsAvailableLaterAppbar.isVisible = false
            youDontNeedToWaitPlaceholder.root.isVisible = false
            roomsAvailableSoonRecyclerView.isVisible = false
            roomsAvailableLaterRecyclerView.isVisible = false
            roomsAvailableSoonProgressBar.isVisible = false
        }
    }

    private fun bookRoom(room: Room) {
        // TODO: Display Fast Booking Slider screen
    }
}
