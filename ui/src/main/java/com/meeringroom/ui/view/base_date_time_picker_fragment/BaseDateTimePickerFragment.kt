package com.meeringroom.ui.view.base_date_time_picker_fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.core_module.event_time_validation.DateValidationState
import com.example.core_module.event_time_validation.TimeValidationEvent
import com.example.core_module.event_time_validation.TimeValidationState
import com.example.core_module.utils.*
import com.example.core_module.utils.DateTimePickerConstants.MINUTE_TO_ROUND
import com.example.core_module.utils.DateTimePickerConstants.OUTPUT_DATE_FORMAT
import com.example.core_module.utils.TimeUtilsConstants.TIME_FORMAT
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view.base_classes.Inflate
import com.meetingroom.ui.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

abstract class BaseDateTimePickerFragment<VB : ViewBinding>(inflate: Inflate<VB>) :
    BaseFragment<VB>(inflate) {

    abstract var fragmentInactivityLimit: Long
    abstract var onDateSetListener: DatePickerDialog.OnDateSetListener

    lateinit var dateOfEvent: LocalDate
    lateinit var eventStartTime: LocalTime
    lateinit var eventEndTime: LocalTime
    lateinit var role: String
    lateinit var startDatePicker: TextView
    lateinit var endDatePicker: TextView
    lateinit var startTimePicker: TextView
    lateinit var endTimePicker: TextView
    lateinit var timeValidationState: LiveData<TimeValidationState>
    lateinit var dateValidationState: LiveData<DateValidationState>

    private var needMoreTimeJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Locale.setDefault(DEFAULT_LOCALE)
        view.setOnTouchListener { _: View, _: MotionEvent ->
            setTimeOut(fragmentInactivityLimit)
            EVENT_IS_HANDLED
        }
        setRole()
        initDatePickers()
        initTimePickers()
        initViews()
        startDatePicker.setOnClickListener {
            deleteTimeOut()
            DatePickerDialogCreator(requireContext(), dateOfEvent, onDateSetListener).create()
                .show()
        }
        startTimePicker.setOnClickListener {
            showTimePickerDialog(getStartTime(), DateTimePickerConstants.TimePickerTag.START)
        }
        endTimePicker.setOnClickListener {
            showTimePickerDialog(getEndTime(), DateTimePickerConstants.TimePickerTag.END)
        }
        observeTimeValidation()
    }

    abstract fun getStartTime(): String

    abstract fun getEndTime(): String

    abstract fun initDatePickers()

    abstract fun initTimePickers()

    abstract fun setToolbarButtonState()

    abstract fun setEvent(timeValidationEvent: TimeValidationEvent)

    abstract fun setRole()

    private fun showTimePickerDialog(
        timeString: String,
        timePickerTag: DateTimePickerConstants.TimePickerTag
    ) {
        TimePickerDialogCreator(timeString)
            .create()
            .apply {
                isCancelable = false
                addOnPositiveButtonClickListener {
                    when (timePickerTag) {
                        DateTimePickerConstants.TimePickerTag.START -> onStartTimeSet(hour, minute)
                        DateTimePickerConstants.TimePickerTag.END -> onEndTimeSet(hour, minute)
                    }
                }
            }
            .show(childFragmentManager, null)
    }

    protected open fun initViews() {
        startDatePicker.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
        endDatePicker.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
        startTimePicker.text = eventStartTime.roundUpMinute(MINUTE_TO_ROUND).timeToString(
            TIME_FORMAT
        )
        endTimePicker.text = eventEndTime.roundUpMinute(MINUTE_TO_ROUND).timeToString(
            TIME_FORMAT
        )
    }

    protected fun addLifecycleObserver(fragmentResId: Int) {
        findNavController().getBackStackEntry(fragmentResId).lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        setTimeOut(fragmentInactivityLimit)
                    }
                    Lifecycle.Event.ON_PAUSE -> deleteTimeOut()
                    else -> {}
                }
            })
    }

    open fun observeTimeValidation() {
        timeValidationState.observe(viewLifecycleOwner) {
            when (it) {
                is TimeValidationState.InvalidStartTime -> {
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
                    setToolbarButtonState()
                }
                is TimeValidationState.InvalidEndTime -> {
                    endTimePicker.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                    setToolbarButtonState()
                }
                is TimeValidationState.InvalidBothTime -> {
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
                    setToolbarButtonState()
                }
                is TimeValidationState.TimeIsValid -> {
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
                    setToolbarButtonState()
                }
            }
//            HERE?
//            setToolbarButtonState()
        }
    }

    protected open fun observeDateValidation() {
        dateValidationState.observe(viewLifecycleOwner) {
            when (it) {
                is DateValidationState.DateIsValid -> {
                    startDatePicker.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.color_primary_text
                        )
                    )
                    endDatePicker.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.color_primary_text
                        )
                    )
                }
                is DateValidationState.InvalidDate -> {
                    startDatePicker.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                    endDatePicker.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                }
            }
        }
    }

    protected open fun onStartTimeSet(hour: Int, minute: Int) {
        eventStartTime = LocalTime.of(hour, minute).roundUpMinute(MINUTE_TO_ROUND)
        startTimePicker.text = eventStartTime.timeToString(TIME_FORMAT)
    }

    protected open fun onEndTimeSet(hour: Int, minute: Int) {
        eventEndTime = LocalTime.of(hour, minute).roundUpMinute(MINUTE_TO_ROUND)
        endTimePicker.text = eventEndTime.timeToString(TIME_FORMAT)
    }

    protected fun onDateSetImpl(year: Int, month: Int, day: Int) {
        dateOfEvent = LocalDate.of(year, month + DateTimePickerConstants.MONTH_VALUE_OFFSET, day)
        startDatePicker.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
        endDatePicker.text = dateOfEvent.dateToString(OUTPUT_DATE_FORMAT)
    }

    protected fun setTimeOut(inactivityLimit: Long) {
        if (needMoreTimeJob != null) deleteTimeOut()
        needMoreTimeJob = lifecycleScope.launch {
            delay(inactivityLimit)
            callTimeoutDialog()
        }
    }

    private fun deleteTimeOut() = needMoreTimeJob?.cancel()

    protected fun getEventStartDateInMillis(): Long {
        val dateInMillis = dateOfEvent
        return DateTimePickerUtils.stringDateAndTimeToMillis(
            dateInMillis.toString(),
            startTimePicker.text.toString()
        )
    }

    abstract fun callTimeoutDialog()

    companion object {
        private const val EVENT_IS_HANDLED = true
        private val DEFAULT_LOCALE = Locale.UK
    }

    override fun onDestroyView() {
        super.onDestroyView()
        needMoreTimeJob?.cancel()
    }
}