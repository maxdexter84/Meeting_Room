package com.andersen.feature_rooms_screen.rooms_event_grid_fragment.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.andersen.feature_rooms_screen.rooms_event_grid_fragment.model.RoomEvent
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.FragmentRoomsBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*


class RoomsEventGridFragment: BaseFragment<FragmentRoomsBinding>(FragmentRoomsBinding::inflate) {

    private var selectedDateForGrid: String? = null

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

    private fun initCalendar() {
        with(binding.oneWeekCalendar) {
            setDateSelected(CalendarDay.today(), true)
            setCurrentDateColor()
            setOnDateChangedListener { widget, date, selected ->
                selectedDateForGrid = "${date.year}-${date.month}-${date.day}"
                Toast.makeText(requireContext(), "selected $selectedDateForGrid", Toast.LENGTH_SHORT).show()
            }
            setOnTitleClickListener {
                showDatePickerDialog()
            }

        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                selectedDateForGrid = "$year-${monthOfYear + 1}-$dayOfMonth"
                Toast.makeText(requireContext(), "selected $selectedDateForGrid", Toast.LENGTH_SHORT).show()
                val selDate = binding.oneWeekCalendar.selectedDate
                val selectedDayForCalendar = CalendarDay.from(year, monthOfYear + 1, dayOfMonth)
                with(binding.oneWeekCalendar) {
                    setDateSelected(selDate, false)
                    setCurrentDate(selectedDayForCalendar, true)
                    setDateSelected(selectedDayForCalendar, true)
                }
            }, year, month, day
        ).apply {
            datePicker.minDate = System.currentTimeMillis()
            show()
        }
    }

    private fun setCurrentDateColor() {
        binding.oneWeekCalendar.addDecorator(object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay): Boolean {
                return day == CalendarDay.today()
            }

            override fun decorate(view: DayViewFacade) {
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.selector_calendar
                )?.let { view.setBackgroundDrawable(it) }
            }
        })
    }
}
