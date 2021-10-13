package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.core_module.utils.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view_utils.hideKeyboard
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.FragmentModifyUpcomingEventBinding
import com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment.DaggerModifyUpcomingEventFragmentComponent
import com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment.ModifyUpcomingEventFragmentModule
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.model.UserTimeTypes
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.TimeValidationDialogManager
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.ModifyUpcomingEventViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

@SuppressLint("NewApi")
class ModifyUpcomingEventFragment :
    BaseFragment<FragmentModifyUpcomingEventBinding>(FragmentModifyUpcomingEventBinding::inflate),
    DatePickerDialog.OnDateSetListener {

    private val args: ModifyUpcomingEventFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel: ModifyUpcomingEventViewModel

    private lateinit var needMoreTimeJob: Job

    /*@Inject
    lateinit var notificationHelper: NotificationHelper*/

    private lateinit var eventRoom: String
    private lateinit var eventReminderTime: String

    override fun onAttach(context: Context) {
        DaggerModifyUpcomingEventFragmentComponent.builder()
            .modifyUpcomingEventFragmentModule(ModifyUpcomingEventFragmentModule(this))
            .build()
            .inject(this)
        super.onAttach(context)
    }

    @SuppressLint("ClickableViewAccessibility")
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
            observeTimeValidation()

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

        findNavController().getBackStackEntry(R.id.modifyUpcomingEventFragment).lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) { setTimeOut() }
        })
        view.setOnTouchListener { _: View, _: MotionEvent ->
            needMoreTimeJob.cancel()
            setTimeOut()
            true
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

    private fun observeTimeValidation() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            with (binding) {
                when (it) {
                   is TimeValidationDialogManager.ValidationState.InvalidStartTime -> {
                       modifyStartTimePicker.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                       binding.modifyEventToolbar.buttonSaveToolbar.isEnabled = false
                   }
                   is TimeValidationDialogManager.ValidationState.InvalidEndTime -> {
                       modifyEndTimePicker.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                       binding.modifyEventToolbar.buttonSaveToolbar.isEnabled = false
                   }
                   is TimeValidationDialogManager.ValidationState.InvalidBothTime -> {
                       modifyStartTimePicker.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                       modifyEndTimePicker.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                       binding.modifyEventToolbar.buttonSaveToolbar.isEnabled = false
                   }
                   is TimeValidationDialogManager.ValidationState.TimeIsValid -> {
                       modifyStartTimePicker.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                       modifyEndTimePicker.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                       binding.modifyEventToolbar.buttonSaveToolbar.isEnabled = true
                   }
               }
            }
        }

        viewModel.effectLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is TimeValidationDialogManager.ValidationEffect.ShowInvalidTimeDialog -> {
                    showAlertDialog(it.messageId)
                }
                is TimeValidationDialogManager.ValidationEffect.NoEffect -> {
                    setTimeOut()
                }
            }
        }
    }

    /*private fun createNotification(reminderStartTime: String) {
        NotificationHelper.setNotification(
            args.upcomingEvent,
            notificationHelper,
            reminderStartTime
        )
    }*/

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

    private fun showDatePickerDialog(dateString: String) {
        deleteTimeOut()
        with(dateString.stringToDate(DATE_FORMAT)) {
            DatePickerDialog(
                requireContext(),
                this@ModifyUpcomingEventFragment,
                year,
                monthValue - 1,
                dayOfMonth
            ).apply {
                datePicker.minDate = System.currentTimeMillis()
                datePicker.maxDate =
                    LocalDateTime.now().plusMonths(MAX_MONTH).atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli()
                show()
            }
        }
    }

    private fun showTimePickerDialog(timeString: String, listener: OnTimeSetListener) {
        deleteTimeOut()
        with(timeString.stringToTime(TIME_FORMAT)) {
            TimePickerDialog(requireContext(), listener, hour, minute, true).apply {
                setTitle(R.string.time_picker_dialog_title)
                show()
            }
        }
    }

    private fun showAlertDialog(messageId: Int) {
        deleteTimeOut()
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(messageId)
            .setCancelable(false)
            .setNegativeButton(R.string.cancel) { _: DialogInterface, _: Int -> setTimeOut() }
            .show()
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        val dateString = LocalDate.of(year, month + 1, day).dateToString(DATE_FORMAT)
        with(binding) {
            modifyStartDatePicker.text = dateString
            modifyEventEndDate.text = dateString
            viewModel.setEvent(
                TimeValidationDialogManager.ValidationEvent.OnDateChanged(
                modifyStartTimePicker.text.toString().stringToTime(TIME_FORMAT),
                dateString.stringToDate(DATE_FORMAT))
            )
        }
    }

    private var startTimePickerListener = OnTimeSetListener { _, hour, minute ->
        with(binding) {
            val startTime = LocalTime.of(hour, minute).roundUpMinute(MINUTE_TO_ROUND)
            modifyStartTimePicker.text = startTime.timeToString(TIME_FORMAT)
            viewModel.setEvent(
                TimeValidationDialogManager.ValidationEvent.OnStartTimeChanged(
                startTime,
                modifyEndTimePicker.text.toString().stringToTime(TIME_FORMAT),
                modifyStartDatePicker.text.toString().stringToDate(DATE_FORMAT)
            ))
        }
    }

    private var endTimePickerListener = OnTimeSetListener { _, hour, minute ->
        with(binding) {
            val endTime = LocalTime.of(hour, minute).roundUpMinute(MINUTE_TO_ROUND)
            modifyEndTimePicker.text = endTime.timeToString(TIME_FORMAT)
            viewModel.setEvent(
                TimeValidationDialogManager.ValidationEvent.OnEndTimeChanged(
                modifyStartTimePicker.text.toString().stringToTime(TIME_FORMAT),
                endTime
            ))
        }
    }

    private fun setTimeOut() {
        needMoreTimeJob = lifecycleScope.launch {
            delay(USER_INACTIVITY_LIMIT)
            findNavController().navigate(ModifyUpcomingEventFragmentDirections.actionModifyUpcomingEventFragmentToNeedMoreTimeDialog())
        }
    }

    private fun deleteTimeOut() = needMoreTimeJob.cancel()

    companion object {
        const val ROOM_KEY = "ROOM_KEY"
        const val TIME_KEY = "TIME_KEY"
        private const val DATE_FORMAT = "d MMM yyyy"
        private const val TIME_FORMAT = "HH:mm"
        private const val MINUTE_TO_ROUND = 5
        private const val MAX_MONTH = 3L
        private const val USER_INACTIVITY_LIMIT = 30000L
    }
}
