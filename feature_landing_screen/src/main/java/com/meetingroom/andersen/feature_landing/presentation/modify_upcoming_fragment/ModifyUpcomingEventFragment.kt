package com.meetingroom.andersen.feature_landing.presentation.modify_upcoming_fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.core_module.component_manager.XInjectionManager
import com.example.core_module.event_time_validation.TimeValidationEffect
import com.example.core_module.event_time_validation.TimeValidationEvent
import com.example.core_module.event_time_validation.TimeValidationState
import com.example.core_module.sharedpreferences.user_data_pref_helper.UserDataPrefHelper
import com.example.core_module.state.State
import com.example.core_module.utils.*
import com.example.core_module.utils.DateTimePickerConstants.INPUT_DATE_FORMAT
import com.example.core_module.utils.TimeUtilsConstants.TIME_FORMAT
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.model.NotificationData
import com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.model.TimePickerData
import com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.presentation.NotificationHelper
import com.meeringroom.ui.view.base_date_time_picker_fragment.BaseDateTimePickerFragment
import com.meeringroom.ui.view_utils.hideKeyboard
import com.meeringroom.ui.view_utils.onClick
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.FragmentModifyUpcomingEventBinding
import com.meetingroom.andersen.feature_landing.domain.entity.ChangedEventDTO
import com.meetingroom.andersen.feature_landing.presentation.di.LandingComponent
import com.meetingroom.andersen.feature_landing.presentation.landing_fragment.LandingFragment.Companion.SUCCESS_KEY
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.util.*
import javax.inject.Inject

class ModifyUpcomingEventFragment :
    BaseDateTimePickerFragment<FragmentModifyUpcomingEventBinding>(FragmentModifyUpcomingEventBinding::inflate),
    DatePickerDialog.OnDateSetListener {

    private val args: ModifyUpcomingEventFragmentArgs by navArgs()

    override var fragmentInactivityLimit = DateTimePickerConstants.EVENT_INACTIVITY_LIMIT
    override var onDateSetListener: DatePickerDialog.OnDateSetListener = this

    private lateinit var eventRoom: String
    private lateinit var eventReminderTime: String
    private var eventReminderStartTime: Int? = null
    private var eventRoomId: Long? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: ModifyUpcomingEventViewModel by viewModels {
        viewModelFactory
    }

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var userDataPrefHelper: UserDataPrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.findComponent<LandingComponent>().inject(this)
        val config = resources.configuration
        dateOfEvent = args.upcomingEvent.eventDate.stringToDate(INPUT_DATE_FORMAT)
        eventStartTime = args.upcomingEvent.startTime.stringToTime(TIME_FORMAT)
        eventEndTime = args.upcomingEvent.endTime.stringToTime(TIME_FORMAT)
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
            tvDeleteEvent.setOnClickListener {
                showDeleteEventDialog()
            }
        }
        observeTimeChange()
        observeRoomChange()
        addLifecycleObserver(R.id.modifyUpcomingEventFragment)
    }

    override fun onStart() {
        super.onStart()
        initViews()
    }

    override fun setRole() {
        val userRole = viewModel.userDataPrefHelper.getUserRole()
        role = userRole ?: getString(R.string.userRole)
    }

    override fun initDatePickers() {
        startDatePicker = binding.modifyStartDatePicker
        endDatePicker = binding.modifyEventEndDate
    }

    override fun initTimePickers() {
        startTimePicker = binding.modifyStartTimePicker
        endTimePicker = binding.modifyEndTimePicker
    }

    override fun initViews() {
        super.initViews()
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
            eventRoomName.text = args.upcomingEvent.room
            getRooms()
        }
    }

    override fun setToolbarButtonState() {
        val timeIsValid = timeValidationState.value == TimeValidationState.TimeIsValid
        binding.modifyEventToolbar.buttonSaveToolbar.isEnabled = timeIsValid
    }

    override fun getStartTime(): String {
        return startTimePicker.text.toString()
    }

    override fun getEndTime(): String {
        return endTimePicker.text.toString()
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

    override fun observeTimeValidation() {
        timeValidationState = viewModel.stateLiveData
        super.observeTimeValidation()
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            getRooms()
        }
        viewModel.effectLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is TimeValidationEffect.ShowInvalidTimeDialog -> {
                    showAlertDialog(it.messageId)
                }
                is TimeValidationEffect.TimeIsValidEffect -> setTimeOut(fragmentInactivityLimit)
            }
        }
    }

    private fun observeTimeChange() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<TimePickerData>(
            TIME_KEY
        )?.observe(viewLifecycleOwner) {
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

    override fun setEvent(timeValidationEvent: TimeValidationEvent) {
        viewModel.setEvent(timeValidationEvent)
    }

    private fun deleteEvent(eventId: Long){
        lifecycleScope.launch {
            viewModel.deleteEvent(eventId)
            deleteReminder(eventId)
        }
        loadingStateObserver()
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

    private fun getRooms() {
        lifecycleScope.launch {
            val startDateTime = "${dateOfEvent}T${binding.modifyStartTimePicker.text}"
            val endDateTime = "${dateOfEvent}T${binding.modifyEndTimePicker.text}"
            viewModel.getRoomsEvent(startDateTime, endDateTime)
        }
    }

    private fun deleteReminder(eventId: Long) {
        userDataPrefHelper.deleteReminder(eventId)
        val listReminders = userDataPrefHelper.getEventIdsForReminder()?.toMutableList()
        listReminders?.removeIf { it == eventId.toString() }
        if (!listReminders.isNullOrEmpty()) {
            userDataPrefHelper.saveEventIdsForReminder(listReminders.toSet())
        }
    }

    private fun showAlertDialog(messageId: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(messageId)
            .setCancelable(false)
            .setNegativeButton(R.string.cancel_button) { _: DialogInterface, _: Int -> setTimeOut(fragmentInactivityLimit) }
            .show()
    }

    override fun callTimeoutDialog() {
        findNavController().navigate(ModifyUpcomingEventFragmentDirections.actionModifyUpcomingEventFragmentToNeedMoreTimeDialog())
    }

    private fun showDeleteEventDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.delete_the_event))
            .setCancelable(false)
            .setNegativeButton(R.string.cancel_button) { dialog, _ -> dialog.cancel() }
            .setPositiveButton(R.string.delete) { _, _ -> deleteEvent(args.upcomingEvent.id) }
            .show()
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

    override fun onStartTimeSet(hour: Int, minute: Int) {
        super.onStartTimeSet(hour, minute)
        setEvent(
            TimeValidationEvent.UserTimeValidationEvent.OnStartTimeChanged(
                eventStartTime,
                eventEndTime,
                dateOfEvent
            )
        )
    }

    override fun onEndTimeSet(hour: Int, minute: Int) {
        super.onEndTimeSet(hour, minute)
        setEvent(
            TimeValidationEvent.UserTimeValidationEvent.OnEndTimeChanged (
                eventStartTime,
                eventEndTime,
                dateOfEvent
            )
        )
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        super.onDateSetImpl(year, month, day)
        setEvent(
            TimeValidationEvent.UserTimeValidationEvent.OnDateChanged (
                eventStartTime,
                dateOfEvent
            )
        )
        getRooms()
    }

    companion object {
        const val ROOM_KEY = "ROOM_KEY"
        const val TIME_KEY = "TIME_KEY"
        private val DEFAULT_LOCALE = Locale.UK
        private const val TITLE_MAX_LENGTH = 50
        private const val DESCRIPTION_MAX_LENGTH = 150
        private const val EXCLUDED_WORD = "before"
        private const val CHANGED = "CHANGED"

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
