package com.andersen.feature_rooms_screen.presentation.rooms_event_grid

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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

    }

    override fun getComponent(): RoomsEventComponent =
        DaggerRoomsEventComponent.builder().build()

    private fun initToolbar() {
        with(binding) {
            roomsToolbar.setToolBarTitle(getString(R.string.toolbar_rooms_title))
        }
    }

    private fun initRecyclerView() {
        val recyclerView = binding.gridRecyclerView
        recyclerView.adapter = viewModel.roomsAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun eventListObserver() {
        lifecycleScope.launch {
            viewModel.mutableRoomEventList.collectLatest {
                viewModel.roomsAdapter.eventList = it
            }
        }
    }

    private fun roomListObserver() {
        lifecycleScope.launch {
            viewModel.mutableRoomList.collectLatest {
                viewModel.roomsAdapter.roomList = it
            }
        }
    }

    private fun loadingStateObserver() {
        lifecycleScope.launch {
            viewModel.mutableState.collectLatest{
                when (it) {
                    is State.Error -> {
                        binding.progressBar.isVisible = false
                    }
                    is State.Loading -> binding.progressBar.isVisible = true
                    is State.NotLoading -> binding.progressBar.isVisible = false
                }
            }
        }
    }
}
