package com.meetingroom.andersen.feature_landing.time_for_notification_dialog.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.RoomAndTimePickerFragmentBinding
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui.ModifyUpcomingEventFragment
import com.meetingroom.andersen.feature_landing.time_for_notification_dialog.model.*

class TimeForNotificationDialog :
    BaseDialogFragment<RoomAndTimePickerFragmentBinding>(RoomAndTimePickerFragmentBinding::inflate) {

    private val timeAdapter by lazy { TimePickerAdapter { setTime(it) } } // todo divide for room and time

    private val args: TimeForNotificationDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initRecyclerView()
        isCancelable = false
    }

    private fun initAdapter() {

        val adapterModels = mutableListOf<TimePickerAdapterModel>()
        adapterModels.apply {
            add(Never(getString(R.string.reminder_disabled_text_for_time)))
            add(Specified(getString(R.string.options_for_reminder_time_one_minute), 1 * MILLIS_IN_MINUTE))
            add(Specified(getString(R.string.options_for_reminder_time_five_minutes), 5 * MILLIS_IN_MINUTE))
            add(Specified(getString(R.string.options_for_reminder_time_ten_minutes), 10 * MILLIS_IN_MINUTE))
            add(Specified(getString(R.string.options_for_reminder_time_fifteen_minutes), 15 * MILLIS_IN_MINUTE))
            add(Specified(getString(R.string.options_for_reminder_time_thirty_minutes), 30 * MILLIS_IN_MINUTE))
            add(Specified(getString(R.string.options_for_reminder_time_one_hour), 1 * MILLIS_IN_HOUR))
            add(Custom(getString(R.string.reminder_custom_text_for_time)))
        }

        adapterModels.map {
            it.isSelected = it.title == args.userSelectedTime
        }

        timeAdapter.time = adapterModels as ArrayList<TimePickerAdapterModel>
    }

    private fun initRecyclerView() {
        with(binding) {
            root.setHasFixedSize(true)
            root.adapter = timeAdapter
        }
    }

    private fun setTime(savedTime: TimePickerAdapterModel) {
        when (savedTime) {
            is Custom -> {
                dismiss()
                findNavController().navigate(TimeForNotificationDialogDirections.actionTimeForNotificationDialogToTimeForNotificationCustomDialog2())
            }
            is Specified -> {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    ModifyUpcomingEventFragment.TIME_KEY,
                    TimePickerData(savedTime.title, savedTime.time, true)
                )
                findNavController().popBackStack()
            }
            is Never -> {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    ModifyUpcomingEventFragment.TIME_KEY,
                    TimePickerData(savedTime.title, 0, true)
                )
                findNavController().popBackStack()
            }
        }
    }

    companion object {
        const val MILLIS_IN_MINUTE = 60000
        const val MILLIS_IN_HOUR = 3600000
        const val MILLIS_IN_DAY = 86400000
    }
}