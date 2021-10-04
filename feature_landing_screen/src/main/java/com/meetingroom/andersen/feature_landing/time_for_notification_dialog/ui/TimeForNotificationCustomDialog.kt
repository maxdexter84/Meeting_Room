package com.meetingroom.andersen.feature_landing.time_for_notification_dialog.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.CustomDialogTimeForNotificationBinding
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui.ModifyUpcomingEventFragment

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
                val timeType =
                    when {
                        customTimeInMinutes.isChecked -> resources.getQuantityString(
                            R.plurals.amountOfMinutes,
                            userCustomTimeEditText.text.toString().toInt()
                        )
                        customTimeInHours.isChecked -> resources.getQuantityString(
                            R.plurals.amountOfHours,
                            userCustomTimeEditText.text.toString().toInt()
                        )
                        customTimeInDays.isChecked -> resources.getQuantityString(
                            R.plurals.amountOfDays,
                            userCustomTimeEditText.text.toString().toInt()
                        )
                        else -> throw IllegalArgumentException(getString(R.string.custom_time_select_type_no_option_error))
                    }
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    ModifyUpcomingEventFragment.TIME_KEY,
                    String.format(
                        getString(R.string.user_selected_custom_time_option),
                        binding.userCustomTimeEditText.text.toString(),
                        timeType
                    )
                )
            }
            dismiss()
        }
    }
}