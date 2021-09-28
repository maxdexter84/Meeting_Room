package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.os.Bundle
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
import com.meetingroom.andersen.feature_landing.utils.dateToString
import com.meetingroom.andersen.feature_landing.utils.stringToDate
import com.meetingroom.andersen.feature_landing.utils.stringToTime
import com.meetingroom.andersen.feature_landing.utils.timeToString
import java.time.LocalDate
import java.time.LocalTime
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
                showTimePickerDialog(modifyStartTimePicker.text.toString(), startTimePickerListener)
            }
            modifyEndTimePicker.setOnClickListener {
                showTimePickerDialog(modifyEndTimePicker.text.toString(), endTimePickerListener)
            }
        }
    }

    private fun createNotification() {
        NotificationHelper.setNotification(args.upcomingEvent, notificationHelper)
    }

    private fun showDatePickerDialog(dateString: String) {
        val localDate = dateString.stringToDate("d MMM yyyy")
        val year = localDate.year
        val month = localDate.monthValue - 1
        val day = localDate.dayOfMonth

        DatePickerDialog(requireContext(), this, year, month, day).show()
    }

    private fun showTimePickerDialog(timeString: String, listener: OnTimeSetListener) {
        val localTime: LocalTime = timeString.stringToTime("HH.mm")
        val hour = localTime.hour
        val minute = localTime.minute

        TimePickerDialog(requireContext(), listener, hour, minute, true).apply {
            setTitle(R.string.time_picker_dialog_title)
            show()
        }
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        val dateString = LocalDate.of(year, month + 1, day).dateToString("d MMM yyyy")
        with(binding) {
            modifyStartDatePicker.text = dateString
            modifyEventEndDate.text = dateString
        }
    }

    private var startTimePickerListener = OnTimeSetListener { _, hour, minute ->
            val timeString = LocalTime.of(hour, minute).timeToString("HH.mm")
            binding.modifyStartTimePicker.text = timeString
    }

    private var endTimePickerListener = OnTimeSetListener { _, hour, minute ->
            val timeString = LocalTime.of(hour, minute).timeToString("HH.mm")
            binding.modifyEndTimePicker.text = timeString
    }
}
