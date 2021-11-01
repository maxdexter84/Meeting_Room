package com.andersen.feature_rooms_screen.rooms_event_grid_fragment.ui

import android.os.Bundle
import android.view.View
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.FragmentRoomsBinding
import android.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.andersen.feature_rooms_screen.rooms_event_grid_fragment.model.RoomEvent
import com.prolificinteractive.materialcalendarview.CalendarDay

class RoomsEventGridFragment: BaseFragment<FragmentRoomsBinding>(FragmentRoomsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initCalendar()
    }

    private fun initToolbar() {
        with(binding) {
            roomsToolbar.setToolBarTitle(getString(R.string.toolbar_rooms_title))
        }
    }

    private fun showDetailsDialog(roomEvent: RoomEvent) {
        val action = RoomsEventGridFragmentDirections.actionRoomsFragmentToEventDetailDialog(roomEvent)
        findNavController().navigate(action)
    }

    private fun initCalendar() {
        with(binding) {
            oneWeekCalendar.setDateSelected(CalendarDay.today(), true)
        }
    }
}
