package com.andersen.feature_rooms_screen.rooms_event_grid_fragment.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.andersen.feature_rooms_screen.rooms_event_grid_fragment.model.RoomEvent
import com.example.core_module.utils.stringToDate
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.FragmentRoomsBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.time.LocalDate
import java.util.*

class RoomsEventGridFragment: BaseFragment<FragmentRoomsBinding>(FragmentRoomsBinding::inflate) {

    private var selectedDateForGrid: LocalDate? = null

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
            setCurrentDateColor()
            setOnDateChangedListener { widget, date, selected ->
                selectedDateForGrid =
                    "${date.day}/${date.month}/${date.year}".stringToDate(DATE_FORMAT1)
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

        DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                selectedDateForGrid =
                    "$dayOfMonth/${monthOfYear + 1}/$year".stringToDate(DATE_FORMAT1)
                val selectedDayForCalendar = CalendarDay.from(year, monthOfYear + 1, dayOfMonth)
                with(binding.oneWeekCalendar) {
                    setDateSelected(selectedDate, false)
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

    companion object {
        private const val DATE_FORMAT1 = "d/M/yyyy"
    }
}
