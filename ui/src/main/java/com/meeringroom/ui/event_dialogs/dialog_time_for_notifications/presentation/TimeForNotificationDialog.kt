package com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.presentation

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.model.*
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.ui.R
import com.meetingroom.ui.databinding.RoomAndTimePickerFragmentBinding

class TimeForNotificationDialog :
    BaseDialogFragment<RoomAndTimePickerFragmentBinding>(RoomAndTimePickerFragmentBinding::inflate) {

    private val timeAdapter by lazy { TimePickerAdapter { setTime(it) } }
    private val adapterModels = mutableListOf<TimePickerAdapterModel>()

    private val args: TimeForNotificationDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initRecyclerView()
        isCancelable = false
    }

    private fun initAdapter() {
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
        if (adapterModels.count { it.isSelected } == 0) adapterModels.find { it is Custom }?.isSelected = true
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
            is Custom -> navigateToCustomDialog()
            is Specified -> navigateBack(savedTime.title, savedTime.time)
            is Never -> navigateBack(savedTime.title, 0)
        }
    }

    private fun navigateBack(title: String, time: Int) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            args.key,
            TimePickerData(title, time, true)
        )
        findNavController().popBackStack()
    }

    private fun navigateToCustomDialog() {
        dismiss()
        findNavController().navigate(TimeForNotificationDialogDirections.actionTimeForNotificationDialogToTimeForNotificationCustomDialog(
            args.key,
            if (adapterModels.find { it is Custom }?.isSelected == true) args.userSelectedTime else ""
        ))
    }

    companion object {
        const val MILLIS_IN_MINUTE = 60000
        const val MILLIS_IN_HOUR = 3600000
        const val MILLIS_IN_DAY = 86400000
    }
}
