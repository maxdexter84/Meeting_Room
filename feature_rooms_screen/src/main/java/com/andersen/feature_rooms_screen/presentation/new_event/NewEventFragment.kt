package com.andersen.feature_rooms_screen.presentation.new_event

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
import android.view.MotionEvent
import android.view.View
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andersen.feature_rooms_screen.presentation.RoomsEventViewModel
import com.andersen.feature_rooms_screen.presentation.di.RoomsEventComponent
import com.example.core_module.component_manager.XInjectionManager
import com.example.core_module.event_time_validation.TimeValidationDialogManager
import com.example.core_module.utils.*
import com.example.core_module.utils.TimeUtilsConstants.TIME_FORMAT
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.model.NotificationData
import com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.model.TimePickerData
import com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.presentation.NotificationHelper
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view_utils.hideKeyboard
import com.meeringroom.ui.view_utils.onClick
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.FragmentNewEventBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class NewEventFragment :
    BaseFragment<FragmentNewEventBinding>(FragmentNewEventBinding::inflate),
    DatePickerDialog.OnDateSetListener {

    private val args: NewEventFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: RoomsEventViewModel by viewModels {
        viewModelFactory
    }

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private lateinit var chosenRoomTitle: String
    private lateinit var eventReminderTime: String
    private var eventReminderStartTime: Int? = null
    private lateinit var dateOfEvent: LocalDate

    private lateinit var needMoreTimeJob: Job

    private val onCancelClickListener: (View) -> Unit = {
        binding.root.hideKeyboard(requireContext())
        requireActivity().onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.findComponent<RoomsEventComponent>().inject(this)
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
            chosenRoomTitle = args.roomTitle
            newEventToolbar.toolbarSaveTitle.text = getString(R.string.new_event_toolbar)
            newEventToolbar.toolbarSaveCancel.setOnClickListener(onCancelClickListener)
            eventRoomName.onClick {
                findNavController().navigate(
                    NewEventFragmentDirections.actionNewEventFragmentToRoomPickerDialogFragment(
                        ROOM_KEY,
                        eventRoomName.text.toString(),
                        viewModel.roomPickerList.value
                    )
                )
            }
            reminderLeftTime.onClick {
                findNavController().navigate(
                    NewEventFragmentDirections.actionNewEventFragmentToTimeForNotificationDialog(
                        TIME_KEY,
                        eventReminderTime
                    )
                )
            }
            newEventToolbar.buttonSaveToolbar.setOnClickListener { saveChanges() }

            newEventTitle.filters = arrayOf(InputFilter.LengthFilter(TITLE_MAX_LENGTH))
            userEventDescription.filters = arrayOf(InputFilter.LengthFilter(DESCRIPTION_MAX_LENGTH))

            observeRoomChange()
            observeTimeChange()
            observeTimeValidation()

            startDatePicker.setOnClickListener {
                showDatePickerDialog(dateOfEvent)
            }
            startTimePicker.setOnClickListener {
                showTimePickerDialog(TimePickerTag.START, startTimePicker.text.toString())
            }
            endTimePicker.setOnClickListener {
                showTimePickerDialog(TimePickerTag.END, endTimePicker.text.toString())
            }
        }
        addLifecycleObserver()
        view.setOnTouchListener { _: View, _: MotionEvent ->
            setTimeOut()
            EVENT_IS_HANDLED
        }
    }

    private fun addLifecycleObserver() {
        findNavController().getBackStackEntry(R.id.newEventFragment).lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> setTimeOut()
                    Lifecycle.Event.ON_PAUSE -> deleteTimeOut()
                    else -> {}
                }
            })
    }

    override fun onStart() {
        super.onStart()
        initViews()
    }

    private fun initViews() {
        with(binding) {
            dateOfEvent = args.eventDate
            startDatePicker.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
            endDatePicker.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
            startTimePicker.text = args.eventStartTime.roundUpMinute(MINUTE_TO_ROUND).timeToString(
                TIME_FORMAT
            )
            endTimePicker.text = args.eventEndTime.roundUpMinute(MINUTE_TO_ROUND).plusHours(
                DEFAULT_HOURS_EVENT_LENGTH
            ).timeToString(TIME_FORMAT)
            eventReminderTime = getString(R.string.reminder_disabled_text_for_time)
            reminderLeftTime.text = eventReminderTime
            eventRoomName.text = args.roomTitle
        }
    }

    private fun observeRoomChange() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(ROOM_KEY)
            ?.observe(viewLifecycleOwner) {
                it?.let {
                    binding.eventRoomName.text = it
                    chosenRoomTitle = it
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
            NotificationData(
                binding.newEventTitle.text.toString(),
                binding.eventRoomName.text.toString(),
                binding.startTimePicker.text.toString(),
                binding.reminderLeftTime.text.toString()
            ),
            notificationHelper,
            reminderStartTime
        )
    }

    private fun observeTimeValidation() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is TimeValidationDialogManager.ValidationState.InvalidStartTime -> {
                        startTimePicker.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.red
                            )
                        )
                        binding.newEventToolbar.buttonSaveToolbar.isEnabled = false
                    }
                    is TimeValidationDialogManager.ValidationState.InvalidEndTime -> {
                        endTimePicker.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.red
                            )
                        )
                        binding.newEventToolbar.buttonSaveToolbar.isEnabled = false
                    }
                    is TimeValidationDialogManager.ValidationState.InvalidBothTime -> {
                        startTimePicker.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.red
                            )
                        )
                        endTimePicker.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.red
                            )
                        )
                        binding.newEventToolbar.buttonSaveToolbar.isEnabled = false
                    }
                    is TimeValidationDialogManager.ValidationState.TimeIsValid -> {
                        startTimePicker.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            )
                        )
                        endTimePicker.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            )
                        )
                        binding.newEventToolbar.buttonSaveToolbar.isEnabled = true
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
        eventReminderStartTime?.let {
            createNotification(
                getReminderSetOffTimeInMillis(
                    it.toLong(),
                    getEventStartDateInMillis()
                )
            )
        }
        requireActivity().onBackPressed()
    }

    private fun showDatePickerDialog(date: LocalDate) {
        deleteTimeOut()
        DatePickerDialog(
            requireContext(),
            this,
            date.year,
            date.monthValue - MONTH_VALUE_OFFSET,
            date.dayOfMonth
        ).apply {
            val minDate = LocalDate.now()
            val maxDate = LocalDate.now().plusMonths(MAX_MONTH)
            setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.ok_button), this)
            setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel_button), this)
            datePicker.init(
                date.year,
                date.monthValue - MONTH_VALUE_OFFSET,
                date.dayOfMonth
            ) { datePicker, year, month, day ->
                val localDate = LocalDate.of(year, month + MONTH_VALUE_OFFSET, day)
                when {
                    localDate.isBefore(minDate) -> datePicker.updateDate(
                        minDate.year,
                        minDate.monthValue - 1,
                        minDate.dayOfMonth
                    )
                    localDate.isAfter(maxDate) -> datePicker.updateDate(
                        maxDate.year,
                        maxDate.monthValue - 1,
                        maxDate.dayOfMonth
                    )
                }
            }
            datePicker.minDate = getLocalDateTime(minDate)
            datePicker.maxDate = getLocalDateTime(maxDate)
            datePicker.firstDayOfWeek = Calendar.MONDAY
            setCancelable(false)
            show()
        }
    }

    private fun showTimePickerDialog(tag: TimePickerTag, timeString: String) {
        deleteTimeOut()
        with(timeString.stringToTime(TIME_FORMAT)) {
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hour)
                .setMinute(minute)
                .build()
                .apply {
                    isCancelable = false
                    addOnPositiveButtonClickListener {
                        when (tag) {
                            TimePickerTag.START -> onStartTimeSet(hour, minute)
                            TimePickerTag.END -> onEndTimeSet(hour, minute)
                        }
                    }
                }
                .show(childFragmentManager, null)
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
        dateOfEvent = LocalDate.of(year, month + MONTH_VALUE_OFFSET, day)
        with(binding) {
            startDatePicker.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
            endDatePicker.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
            viewModel.setEvent(
                TimeValidationDialogManager.ValidationEvent.OnDateChanged(
                    startTimePicker.text.toString().stringToTime(TIME_FORMAT),
                    dateOfEvent
                )
            )
        }
    }

    private fun onStartTimeSet(hour: Int, minute: Int) {
        with(binding) {
            val startTime = LocalTime.of(hour, minute).roundUpMinute(MINUTE_TO_ROUND)
            startTimePicker.text = startTime.timeToString(TIME_FORMAT)
            viewModel.setEvent(
                TimeValidationDialogManager.ValidationEvent.OnStartTimeChanged(
                    startTime,
                    endTimePicker.text.toString().stringToTime(TIME_FORMAT),
                    dateOfEvent
                )
            )
        }
    }

    private fun onEndTimeSet(hour: Int, minute: Int) {
        with(binding) {
            val endTime = LocalTime.of(hour, minute).roundUpMinute(MINUTE_TO_ROUND)
            endTimePicker.text = endTime.timeToString(TIME_FORMAT)
            viewModel.setEvent(
                TimeValidationDialogManager.ValidationEvent.OnEndTimeChanged(
                    startTimePicker.text.toString().stringToTime(TIME_FORMAT),
                    endTime,
                    dateOfEvent
                )
            )
        }
    }

    private fun setTimeOut() {
        if (::needMoreTimeJob.isInitialized) deleteTimeOut()
        needMoreTimeJob = lifecycleScope.launch {
            delay(USER_INACTIVITY_LIMIT)
            findNavController().navigate(NewEventFragmentDirections.actionNewEventFragmentToNeedMoreTimeDialog())
        }
    }

    private fun deleteTimeOut() = needMoreTimeJob.cancel()

    private fun getEventStartDateInMillis(): Long {
        val dateInMillis = dateOfEvent
        return stringDateAndTimeToMillis(
            dateInMillis.toString(),
            binding.startTimePicker.text.toString()
        )
    }

    companion object {
        private const val ROOM_KEY = "ROOM_KEY"
        private const val TIME_KEY = "TIME_KEY"
        private const val OUTPUT_DATE_FORMAT = "EEE, d MMM"
        private val DEFAULT_LOCALE = Locale.UK
        private const val TITLE_MAX_LENGTH = 50
        private const val DESCRIPTION_MAX_LENGTH = 150
        private const val MINUTE_TO_ROUND = 5
        private const val MAX_MONTH = 3L
        private const val USER_INACTIVITY_LIMIT = 30000L
        private const val DEFAULT_HOURS_EVENT_LENGTH = 1L
        private const val MONTH_VALUE_OFFSET = 1
        private const val EVENT_IS_HANDLED = true
        private enum class TimePickerTag {
            START, END
        }

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

        private fun getLocalDateTime(date: LocalDate) =
            LocalDateTime.of(date, LocalTime.of(0, 0, 0)).atZone(ZoneId.systemDefault()).toInstant()
                .toEpochMilli()
    }
}
