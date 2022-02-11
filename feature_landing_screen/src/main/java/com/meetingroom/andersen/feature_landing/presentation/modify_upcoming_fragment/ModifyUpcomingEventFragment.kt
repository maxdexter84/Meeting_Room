package com.meetingroom.andersen.feature_landing.presentation.modify_upcoming_fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
import android.view.MotionEvent
import android.view.View
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.core_module.component_manager.XInjectionManager
import com.example.core_module.event_time_validation.TimeValidationDialogManager
import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper
import com.example.core_module.state.State
import com.example.core_module.utils.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.model.NotificationData
import com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.model.TimePickerData
import com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.presentation.NotificationHelper
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view_utils.hideKeyboard
import com.meeringroom.ui.view_utils.onClick
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.FragmentModifyUpcomingEventBinding
import com.meetingroom.andersen.feature_landing.domain.entity.ChangedEventDTO
import com.meetingroom.andersen.feature_landing.presentation.di.LandingComponent
import com.meetingroom.andersen.feature_landing.presentation.landing_fragment.LandingFragment.Companion.SUCCESS_KEY
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Locale
import java.util.Calendar
import javax.inject.Inject

class ModifyUpcomingEventFragment :
    BaseFragment<FragmentModifyUpcomingEventBinding>(FragmentModifyUpcomingEventBinding::inflate),
    DatePickerDialog.OnDateSetListener {

    private val args: ModifyUpcomingEventFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: ModifyUpcomingEventViewModel by viewModels {
        viewModelFactory
    }

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var userDataPrefHelper: UserDataPrefHelper

    private lateinit var eventRoom: String
    private var eventRoomId: Long? = null
    private lateinit var eventReminderTime: String
    private var eventReminderStartTime: Int? = null
    private lateinit var dateOfEvent: LocalDate

    private lateinit var needMoreTimeJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.findComponent<LandingComponent>().inject(this)
        val config = resources.configuration
        Locale.setDefault(DEFAULT_LOCALE)
        config.setLocale(DEFAULT_LOCALE)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            eventRoom = args.upcomingEvent.room
            modifyEventToolbar.toolbarSaveTitle.text = getString(R.string.modify_event_toolbar)
            modifyEventToolbar.toolbarSaveCancel.setOnClickListener {
                root.hideKeyboard(requireContext())
                findNavController().popBackStack()
            }
            modifyRoomChooser.onClick {
                openRoomsDialog()
            }
            setReminder.onClick {
                findNavController().navigate(
                    ModifyUpcomingEventFragmentDirections.actionModifyUpcomingEventFragmentToTimeForNotificationDialog(
                        TIME_KEY,
                        eventReminderTime
                    )
                )
            }
            modifyEventToolbar.buttonSaveToolbar.onClick { saveChanges() }

            eventRoomName.text = args.upcomingEvent.room
            eventModifyTitle.filters = arrayOf(InputFilter.LengthFilter(TITLE_MAX_LENGTH))
            userEventDescription.filters = arrayOf(InputFilter.LengthFilter(DESCRIPTION_MAX_LENGTH))

            observeRoomChange()
            observeTimeChange()
            observeTimeValidation()

            modifyStartDatePicker.setOnClickListener {
                showDatePickerDialog(dateOfEvent)
            }
            modifyStartTimePicker.setOnClickListener {
                showTimePickerDialog(TimePickerTag.START, modifyStartTimePicker.text.toString())
            }
            modifyEndTimePicker.setOnClickListener {
                showTimePickerDialog(TimePickerTag.END, modifyEndTimePicker.text.toString())
            }

            tvDeleteEvent.setOnClickListener {
                showDeleteEventDialog()
            }
        }
        findNavController().getBackStackEntry(R.id.modifyUpcomingEventFragment).lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    setTimeOut()
                } else if (event == Lifecycle.Event.ON_PAUSE) {
                    deleteTimeOut()
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
                val reminderShort = setReminderTime(requireContext(), args.upcomingEvent.reminderRemainingTime ?: "0")
                reminderLeftTime.text = getString(
                    R.string.reminder_time_for_modify_upcoming_event,
                    getLongReminderLabel(requireContext(), reminderShort)
                )
                eventReminderTime = reminderLeftTime.text.toString()
            } else {
                reminderLeftTime.text = getString(R.string.reminder_disabled_text_for_time)
                args.upcomingEvent.reminderRemainingTime =
                    getString(R.string.reminder_disabled_text_for_time)
                eventReminderTime = getString(R.string.reminder_disabled_text_for_time)
            }
            args.upcomingEvent.description?.let {
                userEventDescription.setText(args.upcomingEvent.description)
            }
            eventModifyTitle.setText(args.upcomingEvent.title)
            modifyStartTimePicker.text = args.upcomingEvent.startTime
            modifyEndTimePicker.text = args.upcomingEvent.endTime
            eventRoomName.text = args.upcomingEvent.room
            dateOfEvent = args.upcomingEvent.eventDate.stringToDate(INPUT_DATE_FORMAT)
            modifyStartDatePicker.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
            modifyEventEndDate.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
            getRooms()
        }
    }

    private fun observeRoomChange() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(ROOM_KEY)
            ?.observe(viewLifecycleOwner) { roomTitleAndId ->
                roomTitleAndId?.let { roomTitleAndId ->
                    eventRoom = roomTitleAndId.filterNot { it.isDigit() }
                    binding.eventRoomName.text = eventRoom
                    eventRoomId = roomTitleAndId.filter { it.isDigit() }.toLong()
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
        with(binding) {
            NotificationHelper.setNotification(
                NotificationData(
                    eventModifyTitle.text.toString(),
                    eventRoomName.text.toString(),
                    modifyStartTimePicker.text.toString(),
                    reminderLeftTime.text.toString().substringBefore(EXCLUDED_WORD)
                ),
                notificationHelper,
                reminderStartTime,
                requireActivity()
            )
        }
    }

    private fun observeTimeValidation() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is TimeValidationDialogManager.ValidationState.InvalidStartTime -> {
                        modifyStartTimePicker.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                        modifyEndTimePicker.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
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
            getRooms()
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
            val changedEvent = ChangedEventDTO(
                description = userEventDescription.text.toString(),
                endDateTime = "${dateOfEvent}T${modifyEndTimePicker.text}",
                id = args.upcomingEvent.id,
                roomId = eventRoomId ?: args.upcomingEvent.roomId,
                startDateTime = "${dateOfEvent}T${modifyStartTimePicker.text}",
                title = eventModifyTitle.text.toString()
            )
            eventReminderStartTime?.let {
                createNotification(
                    getReminderSetOffTimeInMillis(
                        it.toLong(),
                        getEventStartDateInMillis()
                    )
                )
                if(eventReminderStartTime != 0){
                    val listReminders = userDataPrefHelper.getEventIdsForReminder()
                        ?.plus(listOf(changedEvent.id.toString())) ?: listOf(changedEvent.id.toString())
                    userDataPrefHelper.saveEventIdsForReminder(listReminders.toSet())
                    userDataPrefHelper.saveTimeForReminder(changedEvent.id, eventReminderStartTime.toString())
                } else {
                    deleteReminder(args.upcomingEvent.id)
                }
            }
            lifecycleScope.launch {
                viewModel.putChangedEvent(changedEvent)
            }
            loadingStateObserver()
        }
    }

    private fun loadingStateObserver() {
        lifecycleScope.launch {
            viewModel.mutableState.collectLatest {
                when (it) {
                    is State.Loading -> binding.progressBar.isVisible = true
                    else -> {findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        SUCCESS_KEY,
                        CHANGED
                    ); findNavController().popBackStack()}
                }
            }
        }
    }

    private fun showDatePickerDialog(date: LocalDate) {
        deleteTimeOut()
        DatePickerDialog(
            requireContext(),
            this,
            date.year,
            date.monthValue - MONTH_DIFFERENT,
            date.dayOfMonth
        ).apply {
            val minDate = LocalDate.now()
            val maxDate = LocalDate.now().plusMonths(MAX_MONTH)
            setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.ok_button), this)
            setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel_button), this)
            datePicker.init(
                date.year,
                date.monthValue - MONTH_DIFFERENT,
                date.dayOfMonth
            ) { datePicker, year, month, day ->
                val localDate = LocalDate.of(year, month + MONTH_DIFFERENT, day)
                when {
                    localDate.isBefore(minDate) -> datePicker.updateDate(
                        minDate.year,
                        minDate.monthValue - MONTH_DIFFERENT,
                        minDate.dayOfMonth
                    )
                    localDate.isAfter(maxDate) -> datePicker.updateDate(
                        maxDate.year,
                        maxDate.monthValue - MONTH_DIFFERENT,
                        maxDate.dayOfMonth
                    )
                }
            }
            datePicker.minDate =
                LocalDateTime.of(minDate, LocalTime.of(0, 0, 0)).atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli()
            datePicker.maxDate =
                LocalDateTime.of(maxDate, LocalTime.of(0, 0, 0)).atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli()
            datePicker.firstDayOfWeek = Calendar.MONDAY
            setCancelable(false)
            show()
        }
    }

    private fun showTimePickerDialog(tag: TimePickerTag, timeString: String) {
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
        dateOfEvent = LocalDate.of(year, month + MONTH_DIFFERENT, day)
        with(binding) {
            modifyStartDatePicker.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
            modifyEventEndDate.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
            viewModel.setEvent(
                TimeValidationDialogManager.ValidationEvent.OnDateChanged(
                    modifyStartTimePicker.text.toString().stringToTime(TIME_FORMAT),
                    dateOfEvent
                )
            )
        }
        getRooms()
    }

    private fun onStartTimeSet(hour: Int, minute: Int) {
        with(binding) {
            val startTime = LocalTime.of(hour, minute).roundUpMinute(MINUTE_TO_ROUND)
            modifyStartTimePicker.text = startTime.timeToString(TIME_FORMAT)
            viewModel.setEvent(
                TimeValidationDialogManager.ValidationEvent.OnStartTimeChanged(
                    startTime,
                    modifyEndTimePicker.text.toString().stringToTime(TIME_FORMAT),
                    dateOfEvent
                )
            )
        }
    }

    private fun onEndTimeSet(hour: Int, minute: Int) {
        with(binding) {
            val endTime = LocalTime.of(hour, minute).roundUpMinute(MINUTE_TO_ROUND)
            modifyEndTimePicker.text = endTime.timeToString(TIME_FORMAT)
            viewModel.setEvent(
                TimeValidationDialogManager.ValidationEvent.OnEndTimeChanged(
                    modifyStartTimePicker.text.toString().stringToTime(TIME_FORMAT),
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

    private fun showDeleteEventDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.delete_the_event))
            .setCancelable(false)
            .setNegativeButton(R.string.cancel_button) { dialog, _ -> dialog.cancel() }
            .setPositiveButton(R.string.delete) { _, _ -> deleteEvent(args.upcomingEvent.id) }
            .show()
    }

    private fun deleteEvent(eventId: Long){
        lifecycleScope.launch {
            viewModel.deleteEvent(eventId)
            deleteReminder(eventId)
        }
        loadingStateObserver()
    }

    private fun deleteReminder(eventId: Long) {
        userDataPrefHelper.deleteReminder(eventId)
        val listReminders = userDataPrefHelper.getEventIdsForReminder()?.toMutableList()
        listReminders?.removeIf { it == eventId.toString() }
        if (!listReminders.isNullOrEmpty()) {
            userDataPrefHelper.saveEventIdsForReminder(listReminders.toSet())
        }
    }

    private fun getRooms() {
        lifecycleScope.launch {
            val startDateTime = "${dateOfEvent}T${binding.modifyStartTimePicker.text}"
            val endDateTime = "${dateOfEvent}T${binding.modifyEndTimePicker.text}"
            viewModel.getRoomsEvent(startDateTime, endDateTime)
        }
    }

    private fun openRoomsDialog(){
        findNavController().navigate(
            ModifyUpcomingEventFragmentDirections.actionModifyUpcomingEventFragmentToRoomPickerDialogFragment(
                ROOM_KEY,
                eventRoom,
                viewModel.roomPickerArray.value.toTypedArray()
            )
        )
    }

    companion object {
        const val ROOM_KEY = "ROOM_KEY"
        const val TIME_KEY = "TIME_KEY"
        private const val USER_INACTIVITY_LIMIT = 30000L
        private const val INPUT_DATE_FORMAT = "yyyy-MM-d"
        private const val OUTPUT_DATE_FORMAT = "EEE, d MMM"
        private const val TIME_FORMAT = "HH:mm"
        private val DEFAULT_LOCALE = Locale.UK
        private const val TITLE_MAX_LENGTH = 50
        private const val DESCRIPTION_MAX_LENGTH = 150
        private const val MINUTE_TO_ROUND = 5
        private const val MAX_MONTH = 3L
        private const val MONTH_DIFFERENT = 1
        private const val EXCLUDED_WORD = "before"
        private const val CHANGED = "CHANGED"
        private enum class TimePickerTag {
            START, END,
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
    }
}
