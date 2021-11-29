package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
import android.view.MotionEvent
import android.view.View
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.core_module.event_time_validation.TimeValidationDialogManager
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.example.core_module.utils.*
import com.google.android.material.datepicker.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view_utils.hideKeyboard
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.FragmentModifyUpcomingEventBinding
import com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment.DaggerModifyUpcomingEventFragmentComponent
import com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment.ModifyUpcomingEventFragmentModule
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.model.UserTimeTypes
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.ModifyUpcomingEventViewModel
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.NotificationHelper
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.utils.getLongReminderLabel
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.utils.getShortReminderLabel
import com.meetingroom.andersen.feature_landing.time_for_notification_dialog.model.TimePickerData
import kotlinx.coroutines.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.time.*
import java.util.*
import javax.inject.Inject
import java.util.regex.Pattern

class ModifyUpcomingEventFragment :
    BaseFragment<FragmentModifyUpcomingEventBinding>(FragmentModifyUpcomingEventBinding::inflate),
    DatePickerDialog.OnDateSetListener {

    private val args: ModifyUpcomingEventFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel: ModifyUpcomingEventViewModel

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private lateinit var eventRoom: String
    private lateinit var eventReminderTime: String
    private var eventReminderStartTime: Int? = null
    private lateinit var dateOfEvent: LocalDate

    private lateinit var needMoreTimeJob: Job

    override fun onAttach(context: Context) {
        DaggerModifyUpcomingEventFragmentComponent.builder()
            .sharedPreferencesModule(SharedPreferencesModule(requireContext()))
            .modifyUpcomingEventFragmentModule(ModifyUpcomingEventFragmentModule(this))
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Locale.setDefault(DEFAULT_LOCALE)
        resources.configuration.apply {
            setLocale(DEFAULT_LOCALE)
            setLayoutDirection(DEFAULT_LOCALE)
            requireContext().createConfigurationContext(this)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            eventRoom = args.upcomingEvent.eventRoom
            modifyEventToolbar.toolbarSaveTitle.text = getString(R.string.modify_event_toolbar)
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
            eventModifyTitle.filters = arrayOf(InputFilter.LengthFilter(TITLE_MAX_LENGTH), PatternInputFilter(Pattern.compile(ASCII_PATTERN)))
            userEventDescription.filters = arrayOf(InputFilter.LengthFilter(DESCRIPTION_MAX_LENGTH), PatternInputFilter(Pattern.compile(ASCII_PATTERN)))

            observeRoomChange()
            observeTimeChange()
            observeTimeValidation()

            modifyStartDatePicker.setOnClickListener {
                showDatePickerDialog(dateOfEvent)
            }
            modifyStartTimePicker.setOnClickListener {
                showTimePickerDialog(modifyStartTimePicker.text.toString(), startTimePickerListener)
            }
            modifyEndTimePicker.setOnClickListener {
                showTimePickerDialog(modifyEndTimePicker.text.toString(), endTimePickerListener)
            }
        }
        findNavController().getBackStackEntry(R.id.modifyUpcomingEventFragment).lifecycle.addObserver(LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> setTimeOut()
                Lifecycle.Event.ON_PAUSE -> deleteTimeOut()
                else -> {}
            }
        })
        view.setOnTouchListener { _: View, _: MotionEvent ->
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
                reminderLeftTime.text = getLongReminderLabel(requireContext(), args.upcomingEvent.reminderRemainingTime)
                eventReminderTime = reminderLeftTime.text.toString()
            } else {
                reminderLeftTime.text = getString(R.string.reminder_disabled_text_for_time)
                args.upcomingEvent.reminderRemainingTime =
                    getString(R.string.reminder_disabled_text_for_time)
                eventReminderTime = getString(R.string.reminder_disabled_text_for_time)
            }
            args.upcomingEvent.eventDescription?.let {
                userEventDescription.setText(args.upcomingEvent.eventDescription)
            }
            eventModifyTitle.setText(args.upcomingEvent.title)
            modifyStartTimePicker.text = args.upcomingEvent.startTime
            modifyEndTimePicker.text = args.upcomingEvent.endTime
            eventRoomName.text = args.upcomingEvent.eventRoom
            dateOfEvent = args.upcomingEvent.eventDate.stringToDate(INPUT_DATE_FORMAT)
            modifyStartDatePicker.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
            modifyEventEndDate.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
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
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<TimePickerData>(
            TIME_KEY
        )
            ?.observe(viewLifecycleOwner) {
                it?.let {
                    binding.reminderLeftTime.text = it.title
                    eventReminderTime = it.title
                    eventReminderStartTime = it.time
                }
            }
    }

    private fun createNotification(reminderStartTime: Long) {
        NotificationHelper.setNotification(
            args.upcomingEvent,
            notificationHelper,
            reminderStartTime
        )
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
                is TimeValidationDialogManager.ValidationEffect.TimeIsValidEffect -> setTimeOut()
            }
        }
    }

    private fun saveChanges() {
        with(binding) {
            args.upcomingEvent.apply {
                title = eventModifyTitle.text.toString()
                startTime = modifyStartTimePicker.text.toString()
                endTime = modifyEndTimePicker.text.toString()
                eventDate = dateOfEvent.dateToString(INPUT_DATE_FORMAT)
                eventRoom = eventRoomName.text.toString()
                reminderActive =
                    reminderLeftTime.text != getString(UserTimeTypes.fromId(R.string.reminder_disabled_text_for_time).id)
                reminderRemainingTime = getShortReminderLabel(requireContext(), reminderLeftTime.text.toString())
                eventDescription = userEventDescription.text.toString()
            }
            eventReminderStartTime?.let {
                createNotification(
                    getReminderSetOffTimeInMillis(
                        it.toLong(),
                        getEventStartDateInMillis()
                    )
                )
            }
        }
        requireActivity().onBackPressed()
    }

    private fun showDatePickerDialog(date: LocalDate) {
        deleteTimeOut()
        DatePickerDialog(
            requireContext(),
            this,
            date.year,
            date.monthValue - 1,
            date.dayOfMonth
        ).apply {
            val minDate = LocalDate.now()
            val maxDate = LocalDate.now().plusMonths(MAX_MONTH)
            setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.ok_button), this)
            setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel_button), this)
            datePicker.init(date.year, date.monthValue - 1, date.dayOfMonth) { datePicker, year, month, day ->
                val localDate = LocalDate.of(year, month + 1, day)
                when {
                    localDate.isBefore(minDate) -> datePicker.updateDate(minDate.year, minDate.monthValue - 1, minDate.dayOfMonth)
                    localDate.isAfter(maxDate) -> datePicker.updateDate(maxDate.year, maxDate.monthValue - 1, maxDate.dayOfMonth)
                }
            }
            datePicker.minDate = LocalDateTime.of(minDate, LocalTime.of(0, 0, 0)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            datePicker.maxDate = LocalDateTime.of(maxDate, LocalTime.of(0, 0, 0)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            datePicker.firstDayOfWeek = Calendar.MONDAY
            setCancelable(false)
            show()
        }
    }

    private fun showTimePickerDialog(timeString: String, listener: OnTimeSetListener) {
        deleteTimeOut()
        with(timeString.stringToTime(TIME_FORMAT)) {
            TimePickerDialog(requireContext(), listener, hour, minute, true).apply {
                setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.ok), this)
                setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel_button), this)
                setTitle(R.string.time_picker_dialog_title)
                setCancelable(false)
                show()
            }
        }
    }

    private fun showAlertDialog(messageId: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(messageId)
            .setCancelable(false)
            .setNegativeButton(R.string.cancel_button) { _: DialogInterface, _: Int -> setTimeOut() }
            .show()
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        dateOfEvent = LocalDate.of(year, month + 1, day)
        with(binding) {
            modifyStartDatePicker.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
            modifyEventEndDate.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
            viewModel.setEvent(
                TimeValidationDialogManager.ValidationEvent.OnDateChanged(
                modifyStartTimePicker.text.toString().stringToTime(TIME_FORMAT),
                dateOfEvent)
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
                dateOfEvent
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
        if (::needMoreTimeJob.isInitialized) deleteTimeOut()
        needMoreTimeJob = lifecycleScope.launch {
            delay(USER_INACTIVITY_LIMIT)
            findNavController().navigate(ModifyUpcomingEventFragmentDirections.actionModifyUpcomingEventFragmentToNeedMoreTimeDialog())
        }
    }

    private fun deleteTimeOut() = needMoreTimeJob.cancel()

    private fun getEventStartDateInMillis(): Long {
        val dateInMillis = dateOfEvent
        return stringDateAndTimeToMillis(
            dateInMillis.toString(),
            binding.modifyStartTimePicker.text.toString()
        )
    }

    companion object {
        const val ROOM_KEY = "ROOM_KEY"
        const val TIME_KEY = "TIME_KEY"
        private const val USER_INACTIVITY_LIMIT = 30000L
        private const val INPUT_DATE_FORMAT = "d MMM yyyy"
        private const val OUTPUT_DATE_FORMAT = "EEE, d MMM"
        private const val TIME_FORMAT = "HH:mm"
        private const val ASCII_PATTERN = "\\p{ASCII}"
        private val DEFAULT_LOCALE = Locale.UK
        private const val TITLE_MAX_LENGTH = 50
        private const val DESCRIPTION_MAX_LENGTH = 150
        private const val MINUTE_TO_ROUND = 5
        private const val MAX_MONTH = 3L

        fun stringDateAndTimeToMillis(date: String, time: String): Long {
            val dateSegment = date.split("-")
            val timeSegment = time.split(":")
            val dateConstruct = kotlinx.datetime.LocalDateTime(
                dateSegment[0].toInt(),
                dateSegment[1].toInt(),
                dateSegment[2].toInt(),
                timeSegment[0].toInt(),
                timeSegment[1].toInt(),
            )
            return dateConstruct.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        }

        private fun getReminderSetOffTimeInMillis(
            reminderTime: Long,
            eventStartTime: Long
        ): Long {
            val reminderSetOffTime = eventStartTime - reminderTime
            val currentTime = Clock.System.now().toEpochMilliseconds()
            val finalTime = reminderSetOffTime - currentTime
            return currentTime + finalTime
        }
    }
}
