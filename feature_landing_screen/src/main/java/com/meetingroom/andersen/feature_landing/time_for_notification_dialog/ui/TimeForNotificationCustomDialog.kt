package com.meetingroom.andersen.feature_landing.time_for_notification_dialog.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_landing.databinding.CustomDialogTimeForNotificationBinding
import com.meetingroom.andersen.feature_landing.di.time_for_notification_dialog.DaggerTimeForNotificationComponent
import com.meetingroom.andersen.feature_landing.di.time_for_notification_dialog.TimeForNotificationModule
import com.meetingroom.andersen.feature_landing.time_for_notification_dialog.presentation.TimeForNotificationViewModel
import com.meetingroom.andersen.feature_landing.time_for_notification_dialog.presentation.TimeForNotificationViewModelFactory
import javax.inject.Inject

class TimeForNotificationCustomDialog :
    BaseDialogFragment<CustomDialogTimeForNotificationBinding>(
        CustomDialogTimeForNotificationBinding::inflate
    ) {

    @Inject
    lateinit var viewModelFactory: TimeForNotificationViewModelFactory

    private val args: TimeForNotificationCustomDialogArgs by navArgs()

    private val viewModel by activityViewModels<TimeForNotificationViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        DaggerTimeForNotificationComponent.builder()
            .timeForNotificationModule(TimeForNotificationModule())
            .sharedPreferencesModule(SharedPreferencesModule(requireContext()))
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false
        with(binding) {
            customDialogButtonDone.setOnClickListener { navigate() }
        }
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
                viewModel.saveUserTime("In ${userCustomTimeEditText.text} $timeType")
                viewModel.getUserSelectedTime()?.let {
                    args.upcomingEvent.reminderRemainingTime = it
                }
            }
        }
        findNavController().navigate(
            TimeForNotificationCustomDialogDirections.actionTimeForNotificationCustomDialogToModifyUpcomingEventFragment(
                args.upcomingEvent
            )
        )
    }
}