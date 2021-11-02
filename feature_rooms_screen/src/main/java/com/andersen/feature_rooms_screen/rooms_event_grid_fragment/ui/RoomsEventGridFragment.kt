package com.andersen.feature_rooms_screen.rooms_event_grid_fragment.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.andersen.feature_rooms_screen.rooms_event_grid_fragment.model.RoomEvent
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.FragmentRoomsBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

class RoomsEventGridFragment: BaseFragment<FragmentRoomsBinding>(FragmentRoomsBinding::inflate) {

    private var selectedDate: String? = null

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
        val action =
            RoomsEventGridFragmentDirections.actionRoomsFragmentToEventDetailDialog(roomEvent)
        findNavController().navigate(action)
    }

    @SuppressLint("NewApi")
    private fun initCalendar() {
        with(binding.oneWeekCalendar) {
            setDateSelected(CalendarDay.today(), true)
            setOnDateChangedListener { widget, date, selected ->
                Toast.makeText(requireContext(), "click $date", Toast.LENGTH_SHORT).show()
                selectedDate = date
            }
            setOnTitleClickListener {
                showDatePickerDialog()
            }
        }
    }

    @SuppressLint("NewApi")
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(requireContext(),
            DatePickerDialog.OnDateSetListener { view, year1, monthOfYear, dayOfMonth ->
                selectedDate = "CalendarDay{$year1-${monthOfYear + 1}-$dayOfMonth}"
                val selDate = binding.oneWeekCalendar.selectedDate
                binding.oneWeekCalendar.setDateSelected(selDate, false)
                binding.oneWeekCalendar.setDateSelected(
                    CalendarDay.from(
                        year,
                        monthOfYear + 1,
                        dayOfMonth
                    ), true
                )
                Toast.makeText(requireContext(), "$selectedDate", Toast.LENGTH_SHORT).show()
            }, year, month, day
        )
        datePicker.show()
    }
}
