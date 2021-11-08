package com.andersen.feature_rooms_screen.presentation.rooms_event_grid

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andersen.feature_rooms_screen.presentation.di.DaggerRoomsEventComponent
import com.andersen.feature_rooms_screen.presentation.di.RoomsEventComponent
import com.example.core_module.state.State
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.FragmentRoomsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import javax.inject.Inject

class RoomsEventGridFragment : BaseFragment<FragmentRoomsBinding>(FragmentRoomsBinding::inflate), IHasComponent<RoomsEventComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: RoomsEventViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.bindComponent(this).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initRecyclerView()
        eventListObserver()
        roomListObserver()
        loadingStateObserver()
        synchronizationScrolling()
    }

    override fun getComponent(): RoomsEventComponent =
        DaggerRoomsEventComponent.builder().build()

    private fun initToolbar() {
        with(binding) {
            roomsToolbar.setToolBarTitle(getString(R.string.toolbar_rooms_title))
        }
    }

    private fun initRecyclerView() {
        val roomRecyclerView = binding.roomRecyclerView
        val gridRecyclerView = binding.gridRecyclerView
        roomRecyclerView.adapter = viewModel.roomsAdapter
        gridRecyclerView.adapter = viewModel.mainEventAdapter
        roomRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        gridRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun eventListObserver() {
        lifecycleScope.launch {
            viewModel.mutableRoomEventList.collectLatest {
                viewModel.mainEventAdapter.eventList = it
            }
        }
    }

    private fun roomListObserver() {
        lifecycleScope.launch {
            viewModel.mutableRoomList.collectLatest {
                viewModel.roomsAdapter.roomList = it
                viewModel.mainEventAdapter.roomList = it
            }
        }
    }

    private fun loadingStateObserver() {
        lifecycleScope.launch {
            viewModel.mutableState.collectLatest {
                when (it) {
                    is State.Loading -> binding.progressBar.isVisible = true
                    else -> binding.progressBar.isVisible = false
                }
            }
        }
    }

    private fun synchronizationScrolling() {
        with(binding) {
            gridNestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                timeLineView.scrollOnDy(
                    scrollY - oldScrollY
                )
            })

            timeLineView.onScroll = {
                gridNestedScrollView.scrollBy(0, it)
            }

            roomRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (gridRecyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                        super.onScrolled(recyclerView, dx, dy)
                        gridRecyclerView.scrollBy(dx, 0)
                    }
                }
            })

            gridRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (roomRecyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                        super.onScrolled(recyclerView, dx, dy)
                        roomRecyclerView.scrollBy(dx, 0)
                    }
                }
            })
        }
    }
}
