package com.meetingroom.andersen.feature_landing.time_for_notification_dialog.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.CustomDialogTimeForNotificationBinding
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui.ModifyUpcomingEventFragment
import com.meetingroom.andersen.feature_landing.time_for_notification_dialog.model.TimePickerData

class TimeForNotificationCustomDialog :
    BaseDialogFragment<CustomDialogTimeForNotificationBinding>(
        CustomDialogTimeForNotificationBinding::inflate
    ) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        binding.customDialogButtonDone.setOnClickListener { navigate() }
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
                    ModifyUpcomingEventFragment.TIME_KEY,
                    TimePickerData(
                        String.format(
                            getString(R.string.user_selected_custom_time_option),
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
}