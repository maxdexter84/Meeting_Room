package com.andersen.feature_rooms_screen.presentation.new_event.dialog_time_for_notifications

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.andersen.feature_rooms_screen.domain.entity.new_event.TimePickerData
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.CustomDialogTimeForNotificationBinding


class TimeForNotificationCustomDialog :
    BaseDialogFragment<CustomDialogTimeForNotificationBinding>(
        CustomDialogTimeForNotificationBinding::inflate
    ) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        binding.customDialogButtonDone.setOnClickListener { navigate() }
        editTextChangeListener()
    }

    private fun navigate() {
        with(binding) {
            if (userCustomTimeEditText.text.isNotEmpty()) {
                val multiplier: Int
                val timeType: String
                when {
                    customTimeInMinutes.isChecked -> {
                        timeType = resources.getQuantityString(
                            R.plurals.amountOfMinutes,
                            userCustomTimeEditText.text.toString().toInt()
                        )
                        multiplier = TimeForNotificationDialog.MILLIS_IN_MINUTE
                    }
                    customTimeInHours.isChecked -> {
                        timeType = resources.getQuantityString(
                            R.plurals.amountOfHours,
                            userCustomTimeEditText.text.toString().toInt()
                        )
                        multiplier = TimeForNotificationDialog.MILLIS_IN_HOUR
                    }
                    customTimeInDays.isChecked -> {
                        timeType = resources.getQuantityString(
                            R.plurals.amountOfDays,
                            userCustomTimeEditText.text.toString().toInt()
                        )
                        multiplier = TimeForNotificationDialog.MILLIS_IN_DAY
                    }
                    else -> throw IllegalArgumentException(getString(R.string.custom_time_select_type_no_option_error))
                }
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    com.andersen.feature_rooms_screen.presentation.new_event.NewEventFragment.TIME_KEY,
                    TimePickerData(
                        String.format(
                            resources.getString(R.string.user_selected_custom_time_option),
                            binding.userCustomTimeEditText.text.toString(),
                            timeType
                        ),
                        binding.userCustomTimeEditText.text.toString().toInt() * multiplier,
                        true
                    )
                )
            }
            dismiss()
        }
    }

    private fun editTextChangeListener() {
        with(binding) {
            timeFormatCustomDialog.setOnCheckedChangeListener { _, _ -> userCustomTimeEditText.setText("") }
            userCustomTimeEditText.doAfterTextChanged { text ->
                if (text.isNullOrEmpty().not()){
                    val valueTime = text.toString().toInt()
                    when {
                        customTimeInMinutes.isChecked -> if (valueTime > MAX_MINUTES_VALUE) userCustomTimeEditText.setText("$MAX_MINUTES_VALUE")
                        customTimeInHours.isChecked -> if (valueTime > MAX_HOURS_VALUE) userCustomTimeEditText.setText("$MAX_HOURS_VALUE")
                        customTimeInDays.isChecked -> if (valueTime > MAX_DAYS_VALUE) userCustomTimeEditText.setText("$MAX_DAYS_VALUE")
                    }
                }
            }
        }
    }

    companion object {
        const val MAX_MINUTES_VALUE = 180
        const val MAX_HOURS_VALUE = 48
        const val MAX_DAYS_VALUE = 30
    }
}