package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui

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
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view_utils.hideKeyboard
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.FragmentModifyUpcomingEventBinding
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.model.UserTimeTypes
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
    lateinit var notificationHelper: NotificationHelper

    private lateinit var eventRoom: String
    private lateinit var eventReminderTime: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            eventRoom = args.upcomingEvent.eventRoom
            eventReminderTime = args.upcomingEvent.reminderRemainingTime
            modifyEventToolbar.toolbarSaveCancel.setOnClickListener {
                root.hideKeyboard(requireContext())
                requireActivity().onBackPressed()
            }
            modifyRoomChooser.setOnClickListener {
                findNavController().navigate(
                    ModifyUpcomingEventFragmentDirections.actionModifyUpcomingEventFragmentToRoomPickerDialogFragment2(
                        eventRoom
                    )
                )
            }
            setReminder.setOnClickListener {
                findNavController().navigate(
                    ModifyUpcomingEventFragmentDirections.actionModifyUpcomingEventFragmentToTimeForNotificationDialog(
                        eventReminderTime
                    )
                )
            }
            modifyEventToolbar.buttonSaveToolbar.setOnClickListener { saveChanges() }

            eventRoomName.text = args.upcomingEvent.eventRoom

            observeRoomChange()
            observeTimeChange()

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
        initViews()
    }

    private fun initViews() {
        with(binding) {
            if (args.upcomingEvent.reminderActive) {
                reminderLeftTime.text = args.upcomingEvent.reminderRemainingTime
            } else {
                reminderLeftTime.text = getString(R.string.reminder_disabled_text_for_time)
                args.upcomingEvent.reminderRemainingTime =
                    getString(R.string.reminder_disabled_text_for_time)
                eventReminderTime = getString(R.string.reminder_disabled_text_for_time)
            }
            eventModifyTitle.setText(args.upcomingEvent.title)
            modifyStartTimePicker.text = args.upcomingEvent.startTime
            modifyEndTimePicker.text = args.upcomingEvent.endTime
            eventRoomName.text = args.upcomingEvent.eventRoom
            reminderLeftTime.text = args.upcomingEvent.reminderRemainingTime
            modifyStartDatePicker.text = args.upcomingEvent.eventDate
            modifyEventEndDate.text = args.upcomingEvent.eventDate
        }
    }

    private fun observeRoomChange() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(ROOM_KEY)
            ?.observe(viewLifecycleOwner) {
                it?.let {
                    binding.eventRoomName.text = it
                    eventRoom = it
                }
            }
    }

    private fun observeTimeChange() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(TIME_KEY)
            ?.observe(viewLifecycleOwner) {
                it?.let {
                    binding.reminderLeftTime.text = it
                    eventReminderTime = it
                }
            }
    }

    private fun createNotification(reminderStartTime: String) {
        NotificationHelper.setNotification(
            args.upcomingEvent,
            notificationHelper,
            reminderStartTime
        )
    }

    private fun saveChanges() {
        with(binding) {
            args.upcomingEvent.apply {
                title = eventModifyTitle.text.toString()
                startTime = modifyStartTimePicker.text.toString()
                endTime = modifyEndTimePicker.text.toString()
                eventDate = modifyStartDatePicker.text.toString()
                eventRoom = eventRoomName.text.toString()
                reminderActive =
                    reminderLeftTime.text != getString(UserTimeTypes.fromId(R.string.reminder_disabled_text_for_time).id)
                reminderRemainingTime = reminderLeftTime.text.toString()
                eventDescription = userEventDescription.text.toString()
            }
            root.hideKeyboard(requireContext())
        }
        requireActivity().onBackPressed()
    }

    companion object {
        const val ROOM_KEY = "ROOM_KEY"
        const val TIME_KEY = "TIME_KEY"
    }

    private fun showDatePickerDialog(dateString: String) {
        val localDate = dateString.stringToDate(DATE_FORMAT)
        with(localDate) {
            DatePickerDialog(requireContext(), this@ModifyUpcomingEventFragment, year, monthValue - 1, dayOfMonth).apply {
                datePicker.minDate = System.currentTimeMillis()
                datePicker.maxDate = LocalDateTime.now().plusMonths(MAX_MONTH).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
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
            validateStartTime(modifyStartTimePicker.text.toString().stringToTime(TIME_FORMAT))
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
            binding.modifyEventToolbar.buttonSaveToolbar.isEnabled = true
            setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            when {
                startTime.isBefore(LocalTime.now())
                        && binding.modifyStartDatePicker.text.toString().stringToDate(DATE_FORMAT) == LocalDate.now() -> {
                    setRedColorAndDisableSaving(this)
                    showAlertDialog(R.string.event_cant_start_before_current_time_message)
                }
                startTime.isBefore(MIN_TIME) || startTime.isAfter(MAX_TIME) -> {
                    setRedColorAndDisableSaving(this)
                    showAlertDialog(R.string.event_cant_start_between_0_and_6_hours_message)
                }
                else -> validateEndTime(
                    binding.modifyEndTimePicker.text.toString().stringToTime(TIME_FORMAT)
                )
            }
        }
    }

    private fun validateEndTime(endTime: LocalTime) {
        with (binding.modifyEndTimePicker) {
            text = endTime.timeToString(TIME_FORMAT)
            binding.modifyEventToolbar.buttonSaveToolbar.isEnabled = true
            setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            when {
                endTime.isBefore(binding.modifyStartTimePicker.text.toString().stringToTime(TIME_FORMAT)) -> {
                    setRedColorAndDisableSaving(this)
                    showAlertDialog(R.string.event_cant_end_before_it_starts_message)
                }
                endTime.isAfter(binding.modifyStartTimePicker.text.toString().stringToTime(TIME_FORMAT).plusHours(MAX_HOURS_DIFF)) -> {
                    setRedColorAndDisableSaving(this)
                    showAlertDialog(R.string.event_cant_last_longer_than_4_hours_message)
                }
                endTime.isBefore(binding.modifyStartTimePicker.text.toString().stringToTime(TIME_FORMAT).plusMinutes(MIN_MINUTES_DIFF)) -> {
                    setRedColorAndDisableSaving(this)
                    showAlertDialog(R.string.event_cant_last_less_than_15_minutes_message)
                }
                endTime.isBefore(MIN_TIME) || endTime.isAfter(MAX_TIME) -> {
                    setRedColorAndDisableSaving(this)
                    showAlertDialog(R.string.event_cant_end_between_0_and_6_hours_message)
                }
            }
        }
    }

    private fun setRedColorAndDisableSaving(textView: TextView) {
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        binding.modifyEventToolbar.buttonSaveToolbar.isEnabled = false
    }

    companion object {
        const val ROOM_KEY = "ROOM_KEY"
        const val TIME_KEY = "TIME_KEY"
        private const val DATE_FORMAT = "d MMM yyyy"
        private const val TIME_FORMAT = "HH:mm"
        private const val MINUTE_TO_ROUND = 5
        private const val MAX_MONTH = 3L
        private val MIN_TIME = LocalTime.of(6,0)
        private val MAX_TIME = LocalTime.of(23,59)
        private const val MAX_HOURS_DIFF = 4L
        private const val MIN_MINUTES_DIFF = 15L
    }
}
