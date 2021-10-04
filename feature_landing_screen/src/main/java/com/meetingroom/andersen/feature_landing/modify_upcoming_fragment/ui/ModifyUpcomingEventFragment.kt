package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view_utils.hideKeyboard
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.FragmentModifyUpcomingEventBinding
import com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment.DaggerModifyUpcomingEventFragmentComponent
import com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment.ModifyUpcomingEventFragmentModule
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.model.UserTimeTypes
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

    private lateinit var eventRoom: String
    private lateinit var eventReminderTime: String

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
            eventRoom = args.upcomingEvent.eventRoom
            eventReminderTime = args.upcomingEvent.reminderRemainingTime
            modifyEventToolbar.toolbarSaveCancel.setOnClickListener {
                root.hideKeyboard(requireContext())
                requireActivity().onBackPressed()
            }
            modifyEventToolbar.buttonSaveToolbar.setOnClickListener { createNotification() }
            modifyRoomChooser.setOnClickListener {
                findNavController().navigate(
                    ModifyUpcomingEventFragmentDirections.actionModifyUpcomingEventFragmentToRoomPickerDialogFragment2(
                        eventRoom
                    )
                )
            }
            setReminder.setOnClickListener {
                findNavController().navigate(
                    ModifyUpcomingEventFragmentDirections.actionModifyUpcomingEventFragmentToTimeForNotificationDialog(
                        eventReminderTime
                    )
                )
            }
            modifyEventToolbar.buttonSaveToolbar.setOnClickListener { saveChanges() }
            viewModel.userRoom.observe(viewLifecycleOwner) {
                eventRoomName.text = it ?: return@observe
            }
            observeRoomChange()
            observeTimeChange()
        }
    }

    override fun onStart() {
        super.onStart()
        initViews()
    }

    private fun initViews() {
        with(binding) {
            if (args.upcomingEvent.reminderActive) {
                reminderLeftTime.text = args.upcomingEvent.reminderRemainingTime
            } else {
                reminderLeftTime.text = getString(R.string.reminder_disabled_text_for_time)
                args.upcomingEvent.reminderRemainingTime =
                    getString(R.string.reminder_disabled_text_for_time)
                eventReminderTime = getString(R.string.reminder_disabled_text_for_time)
            }
            eventModifyTitle.setText(args.upcomingEvent.title)
            modifyStartTimePicker.text = args.upcomingEvent.startTime
            modifyEndTimePicker.text = args.upcomingEvent.endTime
            eventRoomName.text = args.upcomingEvent.eventRoom
            modifyStartDatePicker.text = args.upcomingEvent.eventDate
            modifyEventEndDate.text = args.upcomingEvent.eventDate
            userEventDescription.setText(args.upcomingEvent.eventDescription ?: "")
            viewModel.update(args.upcomingEvent.eventRoom)
        }
    }

    private fun observeRoomChange() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(ROOM_KEY)
            ?.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.eventRoomName.text = it
                    eventRoom = it
                }
            }
    }

    private fun observeTimeChange() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(TIME_KEY)
            ?.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.reminderLeftTime.text = it
                    eventReminderTime = it
                }
            }
    }

    private fun createNotification() {
        NotificationHelper.setNotification(args.upcomingEvent, notificationHelper)
    }

    private fun saveChanges() {
        with(binding) {
            args.upcomingEvent.apply {
                title = eventModifyTitle.text.toString()
                startTime = modifyStartTimePicker.text.toString()
                endTime = modifyEndTimePicker.text.toString()
                eventDate = modifyStartDatePicker.text.toString()
                eventRoom = eventRoomName.text.toString()
                reminderActive =
                    reminderLeftTime.text != UserTimeTypes.fromId(getString(R.string.reminder_disabled_text_for_time)).id
                reminderRemainingTime = reminderLeftTime.text.toString()
                eventDescription = userEventDescription.text.toString()
            }
            root.hideKeyboard(requireContext())
        }
        requireActivity().onBackPressed()
    }

    companion object {
        const val ROOM_KEY = "ROOM_KEY"
        const val TIME_KEY = "TIME_KEY"
    }
}