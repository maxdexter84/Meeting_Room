package com.andersen.feature_rooms_screen.presentation.new_event

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
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
import com.andersen.feature_rooms_screen.domain.entity.remote.RoomEventCreateDto
import com.andersen.feature_rooms_screen.presentation.RoomsEventViewModel
import com.andersen.feature_rooms_screen.presentation.di.RoomsEventComponent
import com.andersen.feature_rooms_screen.presentation.rooms_event_grid.RoomsEventGridFragment
import com.example.core_module.component_manager.XInjectionManager
import com.example.core_module.event_time_validation.TimeValidationEffect
import com.example.core_module.event_time_validation.TimeValidationEvent
import com.example.core_module.utils.*
import com.example.core_module.event_time_validation.TimeValidationState
import com.example.core_module.state.State
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.meeringroom.ui.event_dialogs.dialog_room_picker.model.RoomPickerNewEventData
import com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.model.NotificationData
import com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.model.TimePickerData
import com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.presentation.NotificationHelper
import com.meeringroom.ui.view.base_date_time_picker_fragment.BaseDateTimePickerFragment
import com.meeringroom.ui.view_utils.hideKeyboard
import com.meeringroom.ui.view_utils.onClick
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.FragmentNewEventBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.util.Locale
import javax.inject.Inject

class NewEventFragment :
    BaseDateTimePickerFragment<FragmentNewEventBinding>(FragmentNewEventBinding::inflate),
    DatePickerDialog.OnDateSetListener {

    private val args: NewEventFragmentArgs by navArgs()

    override var fragmentInactivityLimit = DateTimePickerConstants.EVENT_INACTIVITY_LIMIT
    override var onDateSetListener: DatePickerDialog.OnDateSetListener = this

    private lateinit var eventReminderTime: String
    private lateinit var chosenRoomTitle: String
    private var eventReminderStartTime: Int? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: RoomsEventViewModel by viewModels {
        viewModelFactory
    }

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private var chosenRoomId: Long? = null
    private var isValidTime: Boolean = true
    private var isChoosenRoomFree: Boolean = true

    private val onCancelClickListener: (View) -> Unit = {
        binding.root.hideKeyboard(requireContext())
        findNavController().navigate(NewEventFragmentDirections.actionToRoomsFragment())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.findComponent<RoomsEventComponent>().inject(this)
        dateOfEvent = args.eventDate
        eventStartTime = args.eventStartTime
        eventEndTime = args.eventEndTime
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
            chosenRoomId = args.roomId
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
        }
        observeRoomChange()
        observeTimeChange()
        observeAnotherEventInRooms()
        addLifecycleObserver(R.id.newEventFragment)
    }

    private fun callInitBooking() {
        findNavController().getBackStackEntry(R.id.newEventFragment).lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) initBooking()
            })
    }

    override fun onStart() {
        super.onStart()
        initViews()
        callInitBooking()
    }

    override fun setRole() {
        val userRole = viewModel.userDataPrefHelper.getUserRole()
        role = userRole ?: getString(R.string.userRole)
    }

    override fun initDatePickers() {
        startDatePicker = binding.startDatePicker
        endDatePicker = binding.endDatePicker
    }

    override fun initTimePickers() {
        startTimePicker = binding.startTimePicker
        endTimePicker = binding.endTimePicker
    }

    override fun initViews() {
        super.initViews()
        with(binding) {
            eventReminderTime = getString(R.string.reminder_disabled_text_for_time)
            reminderLeftTime.text = eventReminderTime
            eventRoomName.text = args.roomTitle
            chosenRoomId?.let {
                viewModel.getFreeRoomsList(
                    getStartDateTime(), getEndDateTime(),
                    it
                )
                setToolbarButtonState()
            }
        }
    }

    override fun setToolbarButtonState() {
        if (timeValidationState.value != null) {
            val timeIsValid = timeValidationState.value == TimeValidationState.TimeIsValid
            binding.newEventToolbar.buttonSaveToolbar.isEnabled = timeIsValid && isChoosenRoomFree
        }
    }

    override fun getStartTime(): String {
        return startTimePicker.text.toString()
    }

    override fun getEndTime(): String {
        return endTimePicker.text.toString()
    }

    private fun observeRoomChange() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<RoomPickerNewEventData>(
            ROOM_KEY
        )?.observe(viewLifecycleOwner) {
            it?.let {
                binding.eventRoomName.text = it.titleRoom
                chosenRoomTitle = it.titleRoom
                chosenRoomId = it.idRoom
                viewModel.getFreeRoomsList(getStartDateTime(), getEndDateTime(), it.idRoom)
                setToolbarButtonState()
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
            reminderStartTime,
            requireActivity()
        )
    }


    override fun observeTimeValidation() {
        timeValidationState = viewModel.stateLiveData
        super.observeTimeValidation()
        viewModel.effectLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is TimeValidationEffect.ShowInvalidTimeDialog -> {
                    showAlertDialog(it.messageId)
                }
                is TimeValidationEffect.TimeIsValidEffect -> setTimeOut(
                    fragmentInactivityLimit
                )
            }
        }
        timeValidationState.observe(viewLifecycleOwner) {
            if (it == TimeValidationState.TimeIsValid) {
                isValidTime = true
                chosenRoomId?.let {
                    viewModel.getFreeRoomsList(
                        getStartDateTime(), getEndDateTime(),
                        it
                    )
                }
                initBooking()
                setToolbarButtonState()
            }
        }
    }

    private fun saveChanges() {
       lifecycleScope.launch {  viewModel.activateEvent(
            description = binding.userEventDescription.text.toString(),
            roomId = chosenRoomId ?: 0,
            startDateTime = getStartDateTime(),
            endDateTime = getEndDateTime(),
            title = binding.newEventTitle.text.toString()
        )}
        eventReminderStartTime?.let {
            createNotification(
                getReminderSetOffTimeInMillis(
                    it.toLong(),
                    getEventStartDateInMillis()
                )
            )
        }
        loadingStateObserver()
    }

    private fun loadingStateObserver() {
        lifecycleScope.launch {
            viewModel.mutableState.collectLatest {
                when (it) {
                    is State.Loading -> binding.progressBar.isVisible = true
                    else -> resultOfBooking()
                }
            }
        }
    }

    private fun resultOfBooking() {
        if (viewModel.mutableSaveBookingStaus.value == true) {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                RoomsEventGridFragment.SUCCESS_KEY,
                BOOKED
            )
            findNavController().popBackStack()
        } else {
            binding.progressBar.isVisible = false
            findNavController().navigate(NewEventFragmentDirections.actionNewEventFragmentToTimeOutServerNavigation())
        }
    }

    private fun showAlertDialog(messageId: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(messageId)
            .setCancelable(false)
            .setNegativeButton(R.string.cancel_button) { _: DialogInterface, _: Int ->
                setTimeOut(
                    fragmentInactivityLimit
                )
            }
            .show()
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        super.onDateSetImpl(year, month, day)
        setEvent(
            TimeValidationEvent.UserTimeValidationEvent.OnDateChanged (
                eventStartTime,
                dateOfEvent
            )
        )
        chosenRoomId?.let { viewModel.getFreeRoomsList(getStartDateTime(), getEndDateTime(), it) }
        observeRoomChange()
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

    override fun callTimeoutDialog() {
        findNavController().navigate(NewEventFragmentDirections.actionNewEventFragmentToNeedMoreTimeDialog())
    }

    override fun setEvent(timeValidationEvent: TimeValidationEvent) {
        viewModel.setEvent(timeValidationEvent)
    }

    private fun getStartDateTime() = "${dateOfEvent}T${binding.startTimePicker.text}"

    private fun getEndDateTime() = "${dateOfEvent}T${binding.endTimePicker.text}"

    private fun initBooking() {
        if (chosenRoomId != null && binding.newEventToolbar.buttonSaveToolbar.isEnabled) {
            with(binding) {
                viewModel.createEvent(
                    RoomEventCreateDto(
                        roomId = chosenRoomId ?: 0,
                        description = userEventDescription.text.toString(),
                        startDateTime = getStartDateTime(),
                        endDateTime = getEndDateTime(),
                        title = newEventTitle.text.toString()
                    )
                )
            }
        }
    }

    private fun observeAnotherEventInRooms() {
        lifecycleScope.launch {
            viewModel.mutableEventNow.collectLatest {
                isChoosenRoomFree = it
                binding.eventRoomName.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        if (it) R.color.color_primary else R.color.red
                    )
                )
                setToolbarButtonState()
            }
        }
    }

    companion object {
        private const val ROOM_KEY = "ROOM_KEY"
        private const val TIME_KEY = "TIME_KEY"
        private const val BOOKED = "BOOKED"
        private const val OUTPUT_DATE_FORMAT = "EEE, d MMM"
        private const val VIEW_TIME_AND_DATE_FORMAT = "d MMMyyyyHH:mm"
        private const val TIME_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
        private val DEFAULT_LOCALE = Locale.UK
        private const val TITLE_MAX_LENGTH = 50
        private const val DESCRIPTION_MAX_LENGTH = 150

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
