package com.meetingroom.andersen.feature_landing.time_for_notification_dialog.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meetingroom.andersen.feature_landing.databinding.CustomDialogTimeForNotificationBinding
import com.meetingroom.andersen.feature_landing.databinding.RoomPickerFragmentBinding
import com.meetingroom.andersen.feature_landing.di.time_for_notification_dialog.DaggerTimeForNotificationComponent
import com.meetingroom.andersen.feature_landing.di.time_for_notification_dialog.TimeForNotificationModule
import com.meetingroom.andersen.feature_landing.time_for_notification_dialog.presentation.TimeForNotificationViewModel
import com.meetingroom.andersen.feature_landing.time_for_notification_dialog.presentation.TimeForNotificationViewModelFactory
import javax.inject.Inject

class TimeForNotificationCustomDialog : DialogFragment() {

    private lateinit var binding: CustomDialogTimeForNotificationBinding

    @Inject
    lateinit var viewModelFactory: TimeForNotificationViewModelFactory

    private val viewModel by activityViewModels<TimeForNotificationViewModel> { viewModelFactory }

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
        binding = CustomDialogTimeForNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}