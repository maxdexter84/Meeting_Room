package com.meetingroom.andersen.feature_landing.time_for_notification_dialog.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_landing.databinding.CustomDialogTimeForNotificationBinding
import com.meetingroom.andersen.feature_landing.time_for_notification_dialog.presentation.TimeForNotificationViewModel

class TimeForNotificationCustomDialog :
    BaseDialogFragment<CustomDialogTimeForNotificationBinding>(
        CustomDialogTimeForNotificationBinding::inflate
    ) {

    private val args: TimeForNotificationCustomDialogArgs by navArgs()

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
                viewModel.saveUserTime("${userCustomTimeEditText.text} $timeType before")
                viewModel.userSelectedTime.observe(viewLifecycleOwner) {
                    args.upcomingEvent.reminderRemainingTime = it ?: ""
                }
            }
            if (userCustomTimeEditText.text.isEmpty() && args.upcomingEvent.reminderRemainingTime == "Never") {
                args.upcomingEvent.reminderActive = false
            }
        }
        findNavController().navigate(
            TimeForNotificationCustomDialogDirections.actionTimeForNotificationCustomDialogToModifyUpcomingEventFragment(
                args.upcomingEvent
            )
        )
    }
}