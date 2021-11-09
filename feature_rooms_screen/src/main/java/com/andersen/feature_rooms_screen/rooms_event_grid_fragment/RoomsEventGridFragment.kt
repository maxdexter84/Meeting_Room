package com.andersen.feature_rooms_screen.rooms_event_grid_fragment

import android.os.Bundle
import android.view.View
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.FragmentRoomsBinding
import java.time.LocalTime

class RoomsEventGridFragment: BaseFragment<FragmentRoomsBinding>(FragmentRoomsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }

    private fun initToolbar() {
        with(binding) {
            roomsToolbar.setToolBarTitle(getString(R.string.toolbar_rooms_title))
            binding.timeLineView.dynamicStartTime = LocalTime.of(9, 20, 0)
            binding.timeLineView.dynamicEndTime = LocalTime.of(11, 35, 0)
        }
    }
}
