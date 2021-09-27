package com.meetingroom.andersen.feature_landing.time_for_notification_dialog.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.RoomPickerFragmentBinding
import com.meetingroom.andersen.feature_landing.di.time_for_notification_dialog.DaggerTimeForNotificationComponent
import com.meetingroom.andersen.feature_landing.di.time_for_notification_dialog.TimeForNotificationModule
import com.meetingroom.andersen.feature_landing.room_picker_dialog.model.RoomPickerData
import com.meetingroom.andersen.feature_landing.room_picker_dialog.ui.RoomPickerAdapter
import com.meetingroom.andersen.feature_landing.time_for_notification_dialog.presentation.TimeForNotificationViewModel
import com.meetingroom.andersen.feature_landing.time_for_notification_dialog.presentation.TimeForNotificationViewModelFactory
import javax.inject.Inject

class TimeForNotificationDialog : DialogFragment() {

    private lateinit var binding: RoomPickerFragmentBinding

    @Inject
    lateinit var viewModelFactory: TimeForNotificationViewModelFactory

    private val viewModel by activityViewModels<TimeForNotificationViewModel> { viewModelFactory }

    private val timeAdapter by lazy { RoomPickerAdapter { saveTime(it) }}

    private val args: TimeForNotificationDialogArgs by navArgs()

    override fun onAttach(context: Context) {
        DaggerTimeForNotificationComponent.builder()
            .timeForNotificationModule(TimeForNotificationModule())
            .sharedPreferencesModule(SharedPreferencesModule(requireContext()))
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RoomPickerFragmentBinding.inflate(inflater, container, false)
        return binding.root
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
                timeAdapter.rooms += RoomPickerData(
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
        timeAdapter.rooms.filter {
            it.room != savedTime
        }.map {
            it.isSelected = false
        }
        timeAdapter.rooms.filter {
            it.room == savedTime
        }.map {
            it.isSelected = true
        }
        viewModel.saveUserTime(savedTime)
        viewModel.getUserSelectedTime()?.let {
            args.upcomingEvent.reminderRemainingTime = it
        }
        findNavController().navigate(
            TimeForNotificationDialogDirections.actionTimeForNotificationDialogToModifyUpcomingEventFragment(
                args.upcomingEvent
            )
        )
    }
}