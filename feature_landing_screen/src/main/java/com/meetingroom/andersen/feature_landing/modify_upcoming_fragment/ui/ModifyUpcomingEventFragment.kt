package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import androidx.navigation.fragment.findNavController
import android.widget.DatePicker
import androidx.navigation.fragment.navArgs
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meeringroom.ui.view.base_fragment.BaseFragment
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.FragmentModifyUpcomingEventBinding
import com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment.DaggerModifyUpcomingEventFragmentComponent
import com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment.ModifyUpcomingEventFragmentModule
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.ModifyUpcomingEventViewModel
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.NotificationHelper
import com.example.core_module.utils.dateToString
import com.example.core_module.utils.stringToDate
import com.example.core_module.utils.stringToTime
import com.example.core_module.utils.timeToString
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.datetime.Clock
import kotlinx.datetime.toLocalDate
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@SuppressLint("NewApi")
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
            .setNegativeButton(R.string.cancel, cancelClickListener)
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
        val localTime = LocalTime.of(hour, minute)
        if (localTime.isBefore(minTime) || localTime.isAfter(maxTime)) {
            showAlertDialog(R.string.event_cant_start_between_0_and_6_hours_message)
        }
        if (localTime.isBefore(LocalTime.now())
            && binding.modifyStartDatePicker.text.toString().stringToDate(DATE_FORMAT).equals(LocalDate.now())) {
            showAlertDialog(R.string.event_cant_start_before_current_time_message)
        }
        binding.modifyStartTimePicker.text = localTime.timeToString(TIME_FORMAT)
    }

    private var endTimePickerListener = OnTimeSetListener { _, hour, minute ->
        val localTime = LocalTime.of(hour, minute)
        if (localTime.isBefore(minTime) || localTime.isAfter(maxTime)) {
            showAlertDialog(R.string.event_cant_end_between_0_and_6_hours_message)
            //binding.modifyEndTimePicker.currentTextColor = R.id.
            return@OnTimeSetListener
        }
        if (localTime.isBefore(LocalTime.now())
            && binding.modifyStartDatePicker.text.toString().stringToDate(DATE_FORMAT).equals(LocalDate.now())) {
            showAlertDialog(R.string.event_cant_start_before_current_time_message)
            return@OnTimeSetListener
        }
        if (localTime.isBefore(binding.modifyStartTimePicker.text.toString().stringToTime(TIME_FORMAT))) {
            showAlertDialog(R.string.event_cant_end_before_it_starts_message)
            return@OnTimeSetListener
        }
        if (binding.modifyStartDatePicker.text.toString().stringToTime(TIME_FORMAT).plusHours(4).isAfter(localTime)){
            showAlertDialog(R.string.event_cant_last_longer_than_4_hours_message)
            return@OnTimeSetListener
        }
        binding.modifyEndTimePicker.text = localTime.timeToString(TIME_FORMAT)
    }

    private val cancelClickListener = DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->

    }

    companion object {
        private const val DATE_FORMAT = "d MMM yyyy"
        private const val TIME_FORMAT = "HH:mm"
        private val minTime = LocalTime.of(6,0)
        private val maxTime = LocalTime.of(23,59)
    }
}
