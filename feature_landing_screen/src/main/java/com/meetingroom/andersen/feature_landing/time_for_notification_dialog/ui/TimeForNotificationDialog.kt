package com.meetingroom.andersen.feature_landing.time_for_notification_dialog.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.RoomAndTimePickerFragmentBinding
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.model.UserTimeTypes
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui.ModifyUpcomingEventFragment
import com.meetingroom.andersen.feature_landing.room_picker_dialog.model.RoomAndTimePickerData
import com.meetingroom.andersen.feature_landing.room_picker_dialog.ui.RoomAndTimePickerAdapter

class TimeForNotificationDialog :
    BaseDialogFragment<RoomAndTimePickerFragmentBinding>(RoomAndTimePickerFragmentBinding::inflate) {

    private val timeAdapter by lazy { RoomAndTimePickerAdapter { setTime(it) } }

    private val args: TimeForNotificationDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initRecyclerView()
        isCancelable = false
    }

    private fun initAdapter() {
        requireActivity().resources.getStringArray(R.array.options_for_reminder_time)
            .let { options ->
                val alreadySelectedTime = args.userSelectedTime
                options.forEach { time ->
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

    private fun setTime(savedTime: String) {
        if (savedTime == getString(UserTimeTypes.fromId(R.string.reminder_custom_text_for_time).id)) {
            dismiss()
            findNavController().navigate(TimeForNotificationDialogDirections.actionTimeForNotificationDialogToTimeForNotificationCustomDialog2())
        } else {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                ModifyUpcomingEventFragment.TIME_KEY,
                savedTime
            )
            findNavController().popBackStack()
        }
    }
}