package com.andersen.feature_rooms_screen.presentation.new_lock_event

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andersen.feature_rooms_screen.presentation.di.RoomsEventComponent
import com.example.core_module.component_manager.XInjectionManager
import com.example.core_module.event_time_validation.*
import com.example.core_module.utils.*
import com.example.core_module.utils.DateTimePickerConstants.MINUTE_TO_ROUND
import com.example.core_module.utils.DateTimePickerConstants.MONTH_VALUE_OFFSET
import com.example.core_module.utils.DateTimePickerConstants.OUTPUT_DATE_FORMAT
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.meeringroom.ui.view.base_date_time_picker_fragment.BaseDateTimePickerFragment
import com.meeringroom.ui.view.base_date_time_picker_fragment.DatePickerDialogCreator
import com.meeringroom.ui.view.login_button.MainActionButtonState
import com.meeringroom.ui.view_utils.hideKeyboard
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.FragmentNewLockEventBinding
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class NewLockEventFragment :
    BaseDateTimePickerFragment<FragmentNewLockEventBinding>(FragmentNewLockEventBinding::inflate),
    DatePickerDialog.OnDateSetListener {

    private val args: NewLockEventFragmentArgs by navArgs()

    override var fragmentInactivityLimit = DateTimePickerConstants.LOCK_INACTIVITY_LIMIT
    override var onDateSetListener: DatePickerDialog.OnDateSetListener = this

    private lateinit var startDateOfEvent: LocalDate
    private lateinit var endDateOfEvent: LocalDate

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: NewLockEventViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.findComponent<RoomsEventComponent>().inject(this)
        dateOfEvent = args.eventDate
        startDateOfEvent = dateOfEvent
        endDateOfEvent = dateOfEvent
        eventStartTime = args.eventStartTime
        eventEndTime = args.eventEndTime
        Locale.setDefault(DEFAULT_LOCALE)
        resources.configuration.apply {
            setLocale(DEFAULT_LOCALE)
            setLayoutDirection(DEFAULT_LOCALE)
            requireContext().createConfigurationContext(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        with(binding) {
            newLockEventToolbar.buttonCancel.setOnClickListener {
                binding.root.hideKeyboard(requireContext())
                findNavController().popBackStack()
            }
        }
        observeDateValidation()
        addLifecycleObserver(R.id.newLockEventFragment)
    }

    override fun onStart() {
        initViews()
        super.onStart()
    }

    override fun setRole() {
        role = getString(R.string.adminRole)
    }

    override fun initDatePickers() {
        startDatePicker = binding.startDatePicker
        endDatePicker = binding.endDatePicker
    }

    override fun initTimePickers() {
        startTimePicker = binding.startTimePicker
        endTimePicker = binding.endTimePicker
    }

    override fun getStartTime(): String {
        return startTimePicker.text.toString()
    }

    override fun getEndTime(): String {
        return endTimePicker.text.toString()
    }

    override fun initViews() {
        super.initViews()
        binding.newLockEventToolbar.toolbarTitle.text = getString(R.string.lock_room_toolbar)
        binding.newLockEventToolbar.lockButton.textButton = getString(R.string.button_toolbar_lock)
        binding.startDatePicker.text = args.eventDate.dateToString(OUTPUT_DATE_FORMAT)
        binding.startTimePicker.text = args.eventStartTime.roundUpMinute(MINUTE_TO_ROUND)
            .timeToString(TimeUtilsConstants.TIME_FORMAT)
        binding.endDatePicker.text = args.eventDate.dateToString(OUTPUT_DATE_FORMAT)
        endDatePicker.setOnClickListener(object : DatePickerDialog.OnDateSetListener,
            View.OnClickListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                onEndDateSet(year, month, dayOfMonth)
            }
            override fun onClick(v: View?) {
                DatePickerDialogCreator(requireContext(), endDateOfEvent, this).create()
                    .show()
            }
        })
        binding.endTimePicker.text = args.eventEndTime.roundUpMinute(MINUTE_TO_ROUND)
            .timeToString(TimeUtilsConstants.TIME_FORMAT)
    }

    override fun setToolbarButtonState() {
            if (timeValidationState.value is TimeValidationState.TimeIsValid) {
                binding.newLockEventToolbar.lockButton.state = MainActionButtonState.ENABLED
            } else {
                binding.newLockEventToolbar.lockButton.state = MainActionButtonState.DISABLED
            }
    }

    override fun observeTimeValidation() {
        timeValidationState = viewModel.stateLiveData
        super.observeTimeValidation()
        viewModel.effectLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is TimeValidationEffect.ShowInvalidTimeDialog -> {
                    showAlertDialog(it.messageId)
                }
                is TimeValidationEffect.TimeIsValidEffect -> setTimeOut(fragmentInactivityLimit)
            }
        }
    }

    override fun observeDateValidation() {
        dateValidationState = viewModel.dateStateLiveData
        super.observeDateValidation()
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

    override fun callTimeoutDialog() {
        findNavController().navigate(NewLockEventFragmentDirections.actionNewLockEventFragmentToNeedMoreTimeAdminDialog())
    }

    override fun onStartTimeSet(hour: Int, minute: Int) {
        super.onStartTimeSet(hour, minute)
        setEvent(
            TimeValidationEvent.AdminTimeValidationEvent.OnStartTimeChanged(
                eventStartTime,
                eventEndTime,
                startDateOfEvent,
                endDateOfEvent
            )
        )
    }

    override fun onEndTimeSet(hour: Int, minute: Int) {
        super.onEndTimeSet(hour, minute)
        setEvent(
            TimeValidationEvent.AdminTimeValidationEvent.OnEndTimeChanged(
                eventStartTime,
                eventEndTime,
                startDateOfEvent,
                endDateOfEvent
            )
        )
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        startDateOfEvent = LocalDate.of(year, month + MONTH_VALUE_OFFSET, day)
        startDatePicker.text = startDateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
        setEvent(
            TimeValidationEvent.AdminTimeValidationEvent.OnDateChanged(
                eventStartTime,
                eventEndTime,
                startDateOfEvent,
                endDateOfEvent
            )
        )
    }

    fun onEndDateSet(year: Int, month: Int, day: Int) {
        endDateOfEvent = LocalDate.of(year, month + MONTH_VALUE_OFFSET, day)
        endDatePicker.text = endDateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
        setEvent(
            TimeValidationEvent.AdminTimeValidationEvent.OnDateChanged(
                eventStartTime,
                eventEndTime,
                startDateOfEvent,
                endDateOfEvent
            )
        )
    }

    companion object {
        private val DEFAULT_LOCALE = Locale.UK
    }

    override fun setEvent(timeValidationEvent: TimeValidationEvent) {
        if (timeValidationEvent is TimeValidationEvent.AdminTimeValidationEvent) {
            viewModel.setEvent(timeValidationEvent)
        }
    }
}