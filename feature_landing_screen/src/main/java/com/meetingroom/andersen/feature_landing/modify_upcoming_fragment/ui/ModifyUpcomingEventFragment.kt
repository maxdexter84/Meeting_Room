package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.FragmentModifyUpcomingEventBinding
import com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment.DaggerModifyUpcomingEventFragmentComponent
import com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment.ModifyUpcomingEventFragmentModule
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.ModifyUpcomingEventViewModel
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.NotificationHelper
import javax.inject.Inject

class ModifyUpcomingEventFragment :
    BaseFragment<FragmentModifyUpcomingEventBinding>(FragmentModifyUpcomingEventBinding::inflate) {

    private val args: ModifyUpcomingEventFragmentArgs by navArgs()//? отправить в liveData сразу

    @Inject
    lateinit var viewModel: ModifyUpcomingEventViewModel

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onAttach(context: Context) {
        DaggerModifyUpcomingEventFragmentComponent.builder()
            .modifyUpcomingEventFragmentModule(ModifyUpcomingEventFragmentModule(this))
            .sharedPreferencesModule(SharedPreferencesModule(requireContext()))
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            modifyEventToolbar.toolbarSaveCancel.setOnClickListener {
                requireActivity().onBackPressed()
            }
            modifyEventToolbar.buttonSaveToolbar.setOnClickListener { createNotification() }
            modifyRoomChooser.setOnClickListener {
                findNavController().navigate(
                    ModifyUpcomingEventFragmentDirections.actionModifyUpcomingEventFragmentToRoomPickerDialogFragment2(
                        args.upcomingEvent
                    )
                )
            }
            setReminder.setOnClickListener {
                findNavController().navigate(
                    ModifyUpcomingEventFragmentDirections.actionModifyUpcomingEventFragmentToTimeForNotificationDialog(
                        args.upcomingEvent
                    )
                )
            }
            viewModel.userRoom.observe(viewLifecycleOwner) {
                eventRoomName.text = it ?: return@observe
            }
        }
    }

    override fun onStart() {
        super.onStart()
        initViews()
    }

    private fun initViews() { // TODO: придумать вариант по короче
        with(binding) {
            if (args.upcomingEvent.reminderActive) {
                reminderLeftTime.text = args.upcomingEvent.reminderRemainingTime
            } else {
                reminderLeftTime.text = getString(R.string.reminder_disabled_text_for_time)
                args.upcomingEvent.reminderRemainingTime = getString(R.string.reminder_disabled_text_for_time)
            }
            eventModifyTitle.setText(args.upcomingEvent.title)
            modifyStartTimePicker.text = args.upcomingEvent.startTime
            modifyEndTimePicker.text = args.upcomingEvent.endTime
            eventRoomName.text = args.upcomingEvent.eventRoom
            modifyStartDatePicker.text = args.upcomingEvent.eventDate
            modifyEventEndDate.text = args.upcomingEvent.eventDate
            viewModel.update(args.upcomingEvent.eventRoom)
        }
    }

    private fun createNotification() {
        NotificationHelper.setNotification(args.upcomingEvent, notificationHelper)
    }
}