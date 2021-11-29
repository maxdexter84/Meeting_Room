package com.andersen.feature_rooms_screen.presentation.new_event

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import com.andersen.feature_rooms_screen.domain.entity.new_event.TimePickerData
import com.andersen.feature_rooms_screen.presentation.di.NewEventModule
import com.andersen.feature_rooms_screen.presentation.di.RoomsEventComponent
import com.andersen.feature_rooms_screen.presentation.RoomsEventViewModel
import com.andersen.feature_rooms_screen.presentation.di.DaggerRoomsEventComponent
import com.andersen.feature_rooms_screen.presentation.new_event.dialog_time_for_notifications.NotificationData
import com.andersen.feature_rooms_screen.presentation.new_event.dialog_time_for_notifications.NotificationHelper
import com.example.core_module.event_time_validation.TimeValidationDialogManager
import com.example.core_module.utils.*
import com.example.core_module.utils.TimeUtilsConstants.TIME_FORMAT
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view_utils.hideKeyboard
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.FragmentNewEventBinding
import kotlinx.android.synthetic.main.fragment_new_event.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.time.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Locale
import java.util.Calendar
import javax.inject.Inject
import java.util.regex.Pattern

class NewEventFragment :
    BaseFragment<FragmentNewEventBinding>(FragmentNewEventBinding::inflate),
    IHasComponent<RoomsEventComponent>,
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
        XInjectionManager.bindComponent(this).inject(this)
        Locale.setDefault(DEFAULT_LOCALE)
        resources.configuration.apply {
            setLocale(DEFAULT_LOCALE)
            setLayoutDirection(DEFAULT_LOCALE)
            requireContext().createConfigurationContext(this)
        }
    }

    override fun getComponent(): RoomsEventComponent =
        DaggerRoomsEventComponent.builder()
            .newEventModule(NewEventModule(requireContext()))
            .build()

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            chosenRoomTitle = args.roomTitle
            newEventToolbar.toolbarSaveTitle.text = getString(R.string.new_event_toolbar)
            newEventToolbar.toolbarSaveCancel.setOnClickListener(onCancelClickListener)
            eventRoomName.setOnClickListener {
                findNavController().navigate(
                    NewEventFragmentDirections.actionNewEventFragmentToRoomPickerDialogFragment (
                        eventRoomName.text.toString()
                    )
                )
            }
            reminderLeftTime.setOnClickListener {
                findNavController().navigate(
                    NewEventFragmentDirections.actionNewEventFragmentToTimeForNotificationDialog(
                        eventReminderTime
                    )
                )
            }
            newEventToolbar.buttonSaveToolbar.setOnClickListener { saveChanges() }

            newEventTitle.filters = arrayOf(InputFilter.LengthFilter(TITLE_MAX_LENGTH), PatternInputFilter(Pattern.compile(ASCII_PATTERN)))
            userEventDescription.filters = arrayOf(InputFilter.LengthFilter(DESCRIPTION_MAX_LENGTH), PatternInputFilter(Pattern.compile(ASCII_PATTERN)))

            observeRoomChange()
            observeTimeChange()
            observeTimeValidation()

            startDatePicker.setOnClickListener {
                showDatePickerDialog(dateOfEvent)
            }
            startTimePicker.setOnClickListener {
                showTimePickerDialog(startTimePicker.text.toString(), startTimePickerListener)
            }
            endTimePicker.setOnClickListener {
                showTimePickerDialog(endTimePicker.text.toString(), endTimePickerListener)
            }
        }
        addLifecycleObserver()
        view.setOnTouchListener { _: View, _: MotionEvent ->
            setTimeOut()
            true
        }
    }

    private fun addLifecycleObserver() {
        findNavController().getBackStackEntry(R.id.newEventFragment).lifecycle.addObserver(LifecycleEventObserver { _, event ->
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
            start_date_picker.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
            end_date_picker.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
            startTimePicker.text = LocalTime.now().roundUpMinute(MINUTE_TO_ROUND).timeToString(
                TIME_FORMAT)
            endTimePicker.text = LocalTime.now().roundUpMinute(MINUTE_TO_ROUND).plusHours(
                DEFAULT_HOURS_EVENT_LENGTH).timeToString(TIME_FORMAT)
            eventReminderTime = getString(R.string.reminder_disabled_text_for_time)
            reminderLeftTime.text = eventReminderTime
            eventRoomName.text = viewModel.getFreeRoomsList().first().title
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
            with (binding) {
                when (it) {
                    is TimeValidationDialogManager.ValidationState.InvalidStartTime -> {
                        startTimePicker.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                        binding.newEventToolbar.buttonSaveToolbar.isEnabled = false
                    }
                    is TimeValidationDialogManager.ValidationState.InvalidEndTime -> {
                        endTimePicker.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                        binding.newEventToolbar.buttonSaveToolbar.isEnabled = false
                    }
                    is TimeValidationDialogManager.ValidationState.InvalidBothTime -> {
                        startTimePicker.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                        endTimePicker.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                        binding.newEventToolbar.buttonSaveToolbar.isEnabled = false
                    }
                    is TimeValidationDialogManager.ValidationState.TimeIsValid -> {
                        startTimePicker.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                        endTimePicker.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
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
            datePicker.init(date.year, date.monthValue - MONTH_VALUE_OFFSET, date.dayOfMonth) { datePicker, year, month, day ->
                val localDate = LocalDate.of(year, month + MONTH_VALUE_OFFSET, day)
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

    private fun showTimePickerDialog(timeString: String, listener: TimePickerDialog.OnTimeSetListener) {
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
        dateOfEvent = LocalDate.of(year, month + MONTH_VALUE_OFFSET, day)
        with(binding) {
            startDatePicker.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
            endDatePicker.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
            viewModel.setEvent(
                TimeValidationDialogManager.ValidationEvent.OnDateChanged(
                    startTimePicker.text.toString().stringToTime(TIME_FORMAT),
                    dateOfEvent)
            )
        }
    }

    private var startTimePickerListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
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

    private var endTimePickerListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
        with(binding) {
            val endTime = LocalTime.of(hour, minute).roundUpMinute(MINUTE_TO_ROUND)
            endTimePicker.text = endTime.timeToString(TIME_FORMAT)
            viewModel.setEvent(
                TimeValidationDialogManager.ValidationEvent.OnEndTimeChanged(
                    startTimePicker.text.toString().stringToTime(TIME_FORMAT),
                    endTime
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
        const val ROOM_KEY = "ROOM_KEY"
        const val TIME_KEY = "TIME_KEY"
        private const val OUTPUT_DATE_FORMAT = "EEE, d MMM"
        private const val ASCII_PATTERN = "\\p{ASCII}"
        private val DEFAULT_LOCALE = Locale.UK
        private const val TITLE_MAX_LENGTH = 50
        private const val DESCRIPTION_MAX_LENGTH = 150
        private const val MINUTE_TO_ROUND = 5
        private const val MAX_MONTH = 3L
        private const val USER_INACTIVITY_LIMIT = 30000L
        private const val DEFAULT_HOURS_EVENT_LENGTH = 1L
        private const val MONTH_VALUE_OFFSET = 1

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
