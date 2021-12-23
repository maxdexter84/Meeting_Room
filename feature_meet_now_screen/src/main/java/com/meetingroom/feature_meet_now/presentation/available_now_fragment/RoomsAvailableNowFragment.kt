package com.meetingroom.feature_meet_now.presentation.available_now_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core_module.component_manager.XInjectionManager
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meetingroom.feature_meet_now.domain.entity.Room
import com.meetingroom.feature_meet_now.presentation.di.MeetNowComponent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.findComponent<MeetNowComponent>().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        roomsAvailableNowObserver()
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

    private fun roomsAvailableNowObserver(){
        lifecycleScope.launch {
            viewModel.roomsAvailableNow.collectLatest {
                roomsAdapter.setData(it)
            }
        }
    }

    private fun bookRoom(room: Room) {
        // TODO: Display Fast Booking Slider screen
    }
}
