package com.meetingroom.andersen.feature_landing.time_for_notification_dialog.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_landing.databinding.CustomDialogTimeForNotificationBinding
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui.ModifyUpcomingEventFragment
import com.meetingroom.andersen.feature_landing.time_for_notification_dialog.presentation.TimeForNotificationViewModel

class TimeForNotificationCustomDialog :
    BaseDialogFragment<CustomDialogTimeForNotificationBinding>(
        CustomDialogTimeForNotificationBinding::inflate
    ) {

    private val viewModel by activityViewModels<TimeForNotificationViewModel>()

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
                        customTimeInMinutes.isChecked -> "min"
                        customTimeInHours.isChecked -> "hours"
                        customTimeInDays.isChecked -> "days"
                        else -> ""
                    }
                viewModel.setUserTime("${userCustomTimeEditText.text} $timeType before")
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    ModifyUpcomingEventFragment.TIME_KEY,
                    "${userCustomTimeEditText.text} $timeType before"
                )
            }
            dismiss()
        }
    }
}