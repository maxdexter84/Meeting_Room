package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import com.meeringroom.ui.view.base_fragment.BaseFragment
import com.meetingroom.andersen.feature_landing.databinding.FragmentModifyUpcomingEventBinding
import com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment.DaggerModifyUpcomingEventFragmentComponent
import com.meetingroom.andersen.feature_landing.di.modify_upcoming_fragment.ModifyUpcomingEventFragmentModule
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation.ModifyUpcomingEventViewModel
import javax.inject.Inject

class ModifyUpcomingEventFragment :
    BaseFragment<FragmentModifyUpcomingEventBinding>(FragmentModifyUpcomingEventBinding::inflate) {

    @Inject
    lateinit var viewModel: ModifyUpcomingEventViewModel

    //TODO: add not helper

    override fun onAttach(context: Context) {
        DaggerModifyUpcomingEventFragmentComponent.builder()
            .modifyUpcomingEventFragmentModule(ModifyUpcomingEventFragmentModule(this))
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}