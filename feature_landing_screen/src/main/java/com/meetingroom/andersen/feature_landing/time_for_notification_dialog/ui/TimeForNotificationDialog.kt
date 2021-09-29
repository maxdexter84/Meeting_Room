package com.meetingroom.andersen.feature_landing.time_for_notification_dialog.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.RoomAndTimePickerFragmentBinding
import com.meetingroom.andersen.feature_landing.di.time_for_notification_dialog.DaggerTimeForNotificationComponent
import com.meetingroom.andersen.feature_landing.di.time_for_notification_dialog.TimeForNotificationModule
import com.meetingroom.andersen.feature_landing.room_picker_dialog.model.RoomAndTimePickerData
import com.meetingroom.andersen.feature_landing.room_picker_dialog.ui.RoomAndTimePickerAdapter
import com.meetingroom.andersen.feature_landing.time_for_notification_dialog.presentation.TimeForNotificationViewModel
import com.meetingroom.andersen.feature_landing.time_for_notification_dialog.presentation.TimeForNotificationViewModelFactory
import javax.inject.Inject

class TimeForNotificationDialog :
    BaseDialogFragment<RoomAndTimePickerFragmentBinding>(RoomAndTimePickerFragmentBinding::inflate) {

    @Inject
    lateinit var viewModelFactory: TimeForNotificationViewModelFactory

    private val viewModel by activityViewModels<TimeForNotificationViewModel> { viewModelFactory }

    private val timeAdapter by lazy { RoomAndTimePickerAdapter { saveTime(it) } }

    private val args: TimeForNotificationDialogArgs by navArgs()

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

        initAdapter()
        initRecyclerView()
        isCancelable = false
    }

    private fun initAdapter() {
        requireActivity().resources.getStringArray(R.array.options_for_reminder_time).let {
            val alreadySelectedTime =
                viewModel.getUserSelectedTime()
            it.forEach { time ->
                timeAdapter.roomsAndTime += RoomAndTimePickerData(
                    time,
                    alreadySelectedTime == time,
                    false
                )
            }
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            root.setHasFixedSize(true)
            root.adapter = timeAdapter
        }
    }

    private fun saveTime(savedTime: String) {
        viewModel.changeSelected(timeAdapter.roomsAndTime, savedTime)
        if (savedTime == "Custom...") {
            findNavController().navigate(
                TimeForNotificationDialogDirections.actionTimeForNotificationDialogToTimeForNotificationCustomDialog(
                    args.upcomingEvent
                )
            )
        } else {
            findNavController().navigate(
                TimeForNotificationDialogDirections.actionTimeForNotificationDialogToModifyUpcomingEventFragment(
                    args.upcomingEvent
                )
            )
            viewModel.saveUserTime(savedTime)
            viewModel.getUserSelectedTime()?.let {
                args.upcomingEvent.reminderRemainingTime = it
            }
        }
    }
}