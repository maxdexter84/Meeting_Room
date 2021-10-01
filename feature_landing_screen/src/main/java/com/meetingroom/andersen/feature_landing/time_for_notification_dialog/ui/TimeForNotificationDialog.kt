package com.meetingroom.andersen.feature_landing.time_for_notification_dialog.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.RoomAndTimePickerFragmentBinding
import com.meetingroom.andersen.feature_landing.room_picker_dialog.model.RoomAndTimePickerData
import com.meetingroom.andersen.feature_landing.room_picker_dialog.ui.RoomAndTimePickerAdapter
import com.meetingroom.andersen.feature_landing.time_for_notification_dialog.presentation.TimeForNotificationViewModel

class TimeForNotificationDialog :
    BaseDialogFragment<RoomAndTimePickerFragmentBinding>(RoomAndTimePickerFragmentBinding::inflate) {

    private val viewModel by activityViewModels<TimeForNotificationViewModel>()

    private val timeAdapter by lazy { RoomAndTimePickerAdapter { saveTime(it) } }

    private val args: TimeForNotificationDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initRecyclerView()
        isCancelable = false
        viewModel.saveUserTime(args.upcomingEvent.reminderRemainingTime)
    }

    private fun initAdapter() {
        requireActivity().resources.getStringArray(R.array.options_for_reminder_time)
            .let { options ->
                viewModel.userSelectedTime.observe(viewLifecycleOwner) {
                    val alreadySelectedTime = it
                    options.forEach { time ->
                        timeAdapter.roomsAndTime += RoomAndTimePickerData(
                            time,
                            alreadySelectedTime == time,
                            false
                        )
                    }
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
        args.upcomingEvent.reminderActive = savedTime != "Never"
        if (savedTime == "Custom...") {//TODO Sealed class
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
            viewModel.userSelectedTime.observe(viewLifecycleOwner) {
                args.upcomingEvent.reminderRemainingTime = it ?: ""
            }
        }
    }
}