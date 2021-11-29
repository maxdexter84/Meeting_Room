package com.andersen.feature_rooms_screen.presentation.new_event.dialog_time_for_notifications

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andersen.feature_rooms_screen.domain.entity.new_event.TimePickerData
import com.andersen.feature_rooms_screen.presentation.new_event.NewEventFragment
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.RoomAndTimePickerFragmentBinding

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
            is Custom -> {
                dismiss()
                findNavController().navigate(TimeForNotificationDialogDirections.actionTimeForNotificationDialogToTimeForNotificationCustomDialog2(
                    if (adapterModels.find { it is Custom }?.isSelected == true) args.userSelectedTime else ""
                ))
            }
            is Specified -> navigateToCustomDialog(savedTime.title, savedTime.time)
            is Never -> navigateToCustomDialog(savedTime.title, 0)
        }
    }

    private fun navigateToCustomDialog(title: String, time: Int) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            NewEventFragment.TIME_KEY,
            TimePickerData(title, time, true)
        )
        findNavController().popBackStack()
    }

    companion object {
        const val MILLIS_IN_MINUTE = 60000
        const val MILLIS_IN_HOUR = 3600000
        const val MILLIS_IN_DAY = 86400000
    }
}