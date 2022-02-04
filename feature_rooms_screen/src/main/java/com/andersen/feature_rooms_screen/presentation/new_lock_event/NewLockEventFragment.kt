package com.andersen.feature_rooms_screen.presentation.new_lock_event

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view.login_button.MainActionButtonState
import com.meeringroom.ui.view_utils.hideKeyboard
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.FragmentNewLockEventBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

class NewLockEventFragment :
    BaseFragment<FragmentNewLockEventBinding>(FragmentNewLockEventBinding::inflate),
    DatePickerDialog.OnDateSetListener {

    private val args: NewLockEventFragmentArgs by navArgs()
    private lateinit var dateOfEvent: LocalDate

    private lateinit var needMoreTimeJob: Job

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: RoomsEventViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.findComponent<RoomsEventComponent>().inject(this)
        dateOfEvent = args.eventDate
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
            binding.newLockEventToolbar.buttonCancel.setOnClickListener {
                binding.root.hideKeyboard(requireContext())
                findNavController().popBackStack()
            }
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
        observeTimeValidation()
        addLifecycleObserver()
        view.setOnTouchListener { _: View, _: MotionEvent ->
            setTimeOut()
            EVENT_IS_HANDLED
        }
    }

    private fun initViews() {
        binding.newLockEventToolbar.toolbarTitle.text = getString(R.string.lock_room_toolbar)
        binding.newLockEventToolbar.lockButton.textButton = getString(R.string.button_toolbar_lock)
        binding.startDatePicker.text = args.eventDate.dateToString(OUTPUT_DATE_FORMAT)
        binding.startTimePicker.text = args.eventStartTime.roundUpMinute(MINUTE_TO_ROUND)
            .timeToString(TimeUtilsConstants.TIME_FORMAT)
        binding.endDatePicker.text = args.eventDate.dateToString(OUTPUT_DATE_FORMAT)
        binding.endTimePicker.text = args.eventEndTime.roundUpMinute(MINUTE_TO_ROUND)
            .timeToString(TimeUtilsConstants.TIME_FORMAT)
    }

    private fun addLifecycleObserver() {
        findNavController().getBackStackEntry(R.id.newLockEventFragment).lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> setTimeOut()
                    Lifecycle.Event.ON_PAUSE -> deleteTimeOut()
                    else -> {}
                }
            })
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
                        endTimePicker.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.color_primary_text
                            )
                        )
                        binding.newLockEventToolbar.lockButton.state = MainActionButtonState.DISABLED
                    }
                    is TimeValidationDialogManager.ValidationState.InvalidEndTime -> {
                        endTimePicker.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.red
                            )
                        )
                        binding.newLockEventToolbar.lockButton.state = MainActionButtonState.DISABLED
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
                        binding.newLockEventToolbar.lockButton.state = MainActionButtonState.DISABLED
                    }
                    is TimeValidationDialogManager.ValidationState.TimeIsValid -> {
                        startTimePicker.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.color_primary_text
                            )
                        )
                        endTimePicker.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.color_primary_text
                            )
                        )
                        binding.newLockEventToolbar.lockButton.state = MainActionButtonState.ENABLED
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

    private fun showDatePickerDialog(date: LocalDate) {
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
        with(timeString.stringToTime(TimeUtilsConstants.TIME_FORMAT)) {
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

    private fun onStartTimeSet(hour: Int, minute: Int) {
        with(binding) {
            val startTime = LocalTime.of(hour, minute).roundUpMinute(MINUTE_TO_ROUND)
            startTimePicker.text = startTime.timeToString(TimeUtilsConstants.TIME_FORMAT)
            viewModel.setEvent(
                TimeValidationDialogManager.ValidationEvent.OnStartTimeChanged(
                    startTime,
                    endTimePicker.text.toString().stringToTime(TimeUtilsConstants.TIME_FORMAT),
                    dateOfEvent
                )
            )
        }
    }

    private fun onEndTimeSet(hour: Int, minute: Int) {
        with(binding) {
            val endTime = LocalTime.of(hour, minute).roundUpMinute(MINUTE_TO_ROUND)
            endTimePicker.text = endTime.timeToString(TimeUtilsConstants.TIME_FORMAT)
            viewModel.setEvent(
                TimeValidationDialogManager.ValidationEvent.OnEndTimeChanged(
                    startTimePicker.text.toString().stringToTime(TimeUtilsConstants.TIME_FORMAT),
                    endTime,
                    dateOfEvent
                )
            )
        }
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        dateOfEvent = LocalDate.of(year, month + MONTH_VALUE_OFFSET, day)
        with(binding) {
            startDatePicker.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
            endDatePicker.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
        }
    }

    private fun getLocalDateTime(date: LocalDate) =
        LocalDateTime.of(date, LocalTime.of(0, 0, 0)).atZone(ZoneId.systemDefault()).toInstant()
            .toEpochMilli()

    private fun setTimeOut() {
        if (::needMoreTimeJob.isInitialized) deleteTimeOut()
        needMoreTimeJob = lifecycleScope.launch {
            delay(ADMIN_INACTIVITY_LIMIT)
            findNavController().navigate(
                NewLockEventFragmentDirections.actionNewLockEventFragmentToNeedMoreTimeAdminDialog()
            )
        }
    }

    private fun deleteTimeOut() = needMoreTimeJob.cancel()

    companion object {
        private const val OUTPUT_DATE_FORMAT = "EEE, d MMM"
        private const val MINUTE_TO_ROUND = 5
        private const val MAX_MONTH = 3L
        private const val MONTH_VALUE_OFFSET = 1
        private const val ADMIN_INACTIVITY_LIMIT = 120000L
        private val DEFAULT_LOCALE = Locale.UK
        private const val EVENT_IS_HANDLED = true
        private enum class TimePickerTag {
            START, END
        }
    }
}