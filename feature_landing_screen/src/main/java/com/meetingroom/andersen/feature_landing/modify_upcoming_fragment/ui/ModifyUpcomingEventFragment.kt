package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.navigation.fragment.navArgs
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.meeringroom.ui.view.base_fragment.BaseFragment
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.FragmentModifyUpcomingEventBinding
import com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment.DaggerModifyUpcomingEventFragmentComponent
import com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment.ModifyUpcomingEventFragmentModule
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.ModifyUpcomingEventViewModel
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.NotificationHelper
import com.meetingroom.andersen.feature_landing.utils.dateToString
import com.meetingroom.andersen.feature_landing.utils.stringToDate
import com.meetingroom.andersen.feature_landing.utils.stringToTime
import com.meetingroom.andersen.feature_landing.utils.timeToString
import org.koin.android.ext.android.get
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.*
import javax.inject.Inject

class ModifyUpcomingEventFragment :
    BaseFragment<FragmentModifyUpcomingEventBinding>(FragmentModifyUpcomingEventBinding::inflate) {

    private val args: ModifyUpcomingEventFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel: ModifyUpcomingEventViewModel

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onAttach(context: Context) {
        DaggerModifyUpcomingEventFragmentComponent.builder()
            .modifyUpcomingEventFragmentModule(ModifyUpcomingEventFragmentModule(this))
            .sharedPreferencesModule(SharedPreferencesModule(requireContext()))
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            modifyEventToolbar.toolbarSaveCancel.setOnClickListener {
                requireActivity().onBackPressed()
            }
            modifyEventToolbar.buttonSaveToolbar.setOnClickListener { createNotification() }
        }
    }

    override fun onStart() {
        super.onStart()
        with(binding) {
            eventModifyTitle.setText(args.upcomingEvent.title)
            modifyStartTimePicker.text = args.upcomingEvent.startTime
            modifyEndTimePicker.text = args.upcomingEvent.endTime
            eventRoomName.text = args.upcomingEvent.eventRoom
            reminderLeftTime.text = args.upcomingEvent.reminderRemainingTime
            modifyStartDatePicker.text = args.upcomingEvent.eventDate
            modifyEventEndDate.text = args.upcomingEvent.eventDate

            modifyStartDatePicker.setOnClickListener {
                showDatePickerDialog(modifyStartDatePicker.text.toString())
            }
            modifyStartTimePicker.setOnClickListener {
                showTimePickerDialog(modifyStartTimePicker.text.toString(), START_TIME_PICKER_TAG)
            }
            modifyEndTimePicker.setOnClickListener {
                showTimePickerDialog(modifyEndTimePicker.text.toString(), END_TIME_PICKER_TAG)
            }
        }
    }

    private fun createNotification() {
        NotificationHelper.setNotification(args.upcomingEvent, notificationHelper)
    }

    private fun showDatePickerDialog(dateString: String) {
        val localDate = dateString.stringToDate("d MMM yyyy")
        val picker = MaterialDatePicker.Builder.datePicker()
            .setSelection(localDate.atTime(LocalTime.MIDNIGHT).atZone(ZoneOffset.UTC).toInstant().toEpochMilli())
            .build()
        picker.show(childFragmentManager, DATE_PICKER_TAG)
        picker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = it
            onDateSet(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        }
        //DatePickerDialog(requireContext(), R.style.DatePickerDialog, this, year, month, day).show()
    }

    private fun showTimePickerDialog(timeString: String, tag: String) {
        val localTime: LocalTime = timeString.stringToTime("HH.mm")
        val hour = localTime.hour
        val minute = localTime.minute
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(hour)
            .setMinute(minute)
            .build()
        picker.show(childFragmentManager, tag)
        picker.addOnPositiveButtonClickListener {
            onTimeSet(tag, picker.hour, picker.minute)
        }
        //TimePickerDialog(requireContext(), this, hour, minute, true).show()
    }

    private fun onDateSet(year: Int, month: Int, day: Int) {
        val dateString = LocalDate.of(year, month + 1, day).dateToString("d MMM yyyy")
        with(binding) {
            modifyStartDatePicker.text = dateString
            modifyEventEndDate.text = dateString
        }
    }

    private fun onTimeSet(tag: String, hour: Int, minute: Int) {
        val timeString = LocalTime.of(hour, minute).timeToString("HH.mm")
        if (tag == START_TIME_PICKER_TAG) {
            binding.modifyStartTimePicker.text = timeString
        }
        if (tag == END_TIME_PICKER_TAG) {
            binding.modifyEndTimePicker.text = timeString
        }
    }

    companion object {
        private const val DATE_PICKER_TAG ="DATE_PICKER"
        private const val START_TIME_PICKER_TAG = "START_TIME_PICKER"
        private const val END_TIME_PICKER_TAG = "END_TIME_PICKER"
    }
}