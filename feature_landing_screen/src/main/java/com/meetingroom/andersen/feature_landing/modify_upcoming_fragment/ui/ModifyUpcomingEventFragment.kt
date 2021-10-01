package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import android.widget.DatePicker
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.example.core_module.utils.*
import com.meeringroom.ui.view.base_fragment.BaseFragment
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.FragmentModifyUpcomingEventBinding
import com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment.DaggerModifyUpcomingEventFragmentComponent
import com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment.ModifyUpcomingEventFragmentModule
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.ModifyUpcomingEventViewModel
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.NotificationHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class ModifyUpcomingEventFragment :
    BaseFragment<FragmentModifyUpcomingEventBinding>(FragmentModifyUpcomingEventBinding::inflate), DatePickerDialog.OnDateSetListener {

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
            modifyRoomChooser.setOnClickListener {
                findNavController().navigate(
                    ModifyUpcomingEventFragmentDirections.actionModifyUpcomingEventFragmentToRoomPickerDialogFragment2(
                        args.upcomingEvent
                    )
                )
            }
            modifyStartDatePicker.setOnClickListener {
                showDatePickerDialog(modifyStartDatePicker.text.toString())
            }
            modifyStartTimePicker.setOnClickListener {
                showTimePickerDialog(modifyStartTimePicker.text.toString(), startTimePickerListener)
            }
            modifyEndTimePicker.setOnClickListener {
                showTimePickerDialog(modifyEndTimePicker.text.toString(), endTimePickerListener)
            }
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
        }
    }

    private fun createNotification() {
        NotificationHelper.setNotification(args.upcomingEvent, notificationHelper)
    }

    private fun showDatePickerDialog(dateString: String) {
        val localDate = dateString.stringToDate(DATE_FORMAT)
        with(localDate) {
            DatePickerDialog(requireContext(), this@ModifyUpcomingEventFragment, year, monthValue - 1, dayOfMonth).apply {
                datePicker.minDate = System.currentTimeMillis()
                datePicker.maxDate = LocalDateTime.now().plusMonths(3).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                show()
            }
        }
    }

    private fun showTimePickerDialog(timeString: String, listener: OnTimeSetListener) {
        val localTime: LocalTime = timeString.stringToTime(TIME_FORMAT)
        with(localTime) {
            TimePickerDialog(requireContext(), listener, hour, minute, true).apply {
                setTitle(R.string.time_picker_dialog_title)
                show()
            }
        }
    }

    private fun showAlertDialog(messageId: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(messageId)
            .setCancelable(false)
            .setNegativeButton(R.string.cancel) { _: DialogInterface, _: Int -> }
            .show()
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        val dateString = LocalDate.of(year, month + 1, day).dateToString(DATE_FORMAT)
        with(binding) {
            modifyStartDatePicker.text = dateString
            modifyEventEndDate.text = dateString
        }
    }

    private var startTimePickerListener = OnTimeSetListener { _, hour, minute ->
        validateStartTime(LocalTime.of(hour, minute).roundUpMinute(MINUTE_TO_ROUND))
    }

    private var endTimePickerListener = OnTimeSetListener { _, hour, minute ->
        validateEndTime(LocalTime.of(hour, minute).roundUpMinute(MINUTE_TO_ROUND))
    }

    private fun validateStartTime(startTime: LocalTime) {
        with (binding.modifyStartTimePicker) {
            text = startTime.timeToString(TIME_FORMAT)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            if (startTime.isBefore(LocalTime.now())
                && binding.modifyStartDatePicker.text.toString().stringToDate(DATE_FORMAT) == LocalDate.now()) {
                setRedColor(this)
                showAlertDialog(R.string.event_cant_start_before_current_time_message)
            } else if (startTime.isBefore(MIN_TIME) || startTime.isAfter(MAX_TIME)) {
                setRedColor(this)
                showAlertDialog(R.string.event_cant_start_between_0_and_6_hours_message)
            }
        }
    }

    private fun validateEndTime(endTime: LocalTime) {
        with (binding.modifyEndTimePicker) {
            text = endTime.timeToString(TIME_FORMAT)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            if (endTime.isBefore(binding.modifyStartTimePicker.text.toString().stringToTime(TIME_FORMAT))) {
                setRedColor(this)
                showAlertDialog(R.string.event_cant_end_before_it_starts_message)
            } else if (endTime.isAfter(binding.modifyStartTimePicker.text.toString().stringToTime(TIME_FORMAT).plusHours(4))) {
                setRedColor(this)
                showAlertDialog(R.string.event_cant_last_longer_than_4_hours_message)
            } else if (endTime.isBefore(MIN_TIME) || endTime.isAfter(MAX_TIME)) {
                setRedColor(this)
                showAlertDialog(R.string.event_cant_end_between_0_and_6_hours_message)
            }
        }
    }

    private fun setRedColor(textView: TextView) {
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
    }

    companion object {
        private const val DATE_FORMAT = "d MMM yyyy"
        private const val TIME_FORMAT = "HH:mm"
        private val MIN_TIME = LocalTime.of(6,0)
        private val MAX_TIME = LocalTime.of(23,59)
        private const val MINUTE_TO_ROUND = 5
    }
}
