package com.meetingroom.feature_workplaces_screen.presentation.fragments.workplaces_fragment

import android.os.Bundle
import android.view.View
import com.example.core_module.component_manager.IHasComponent
import com.example.core_module.component_manager.XInjectionManager
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meetingroom.feature_workplaces_screen.R
import com.meetingroom.feature_workplaces_screen.databinding.FragmentWorkplacesBinding
import com.meetingroom.feature_workplaces_screen.presentation.di.DaggerWorkplacesComponent
import com.meetingroom.feature_workplaces_screen.presentation.di.WorkplacesComponent

class WorkplacesFragment :
    BaseFragment<FragmentWorkplacesBinding>(FragmentWorkplacesBinding::inflate),
    IHasComponent<WorkplacesComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.bindComponent(this).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }

    override fun getComponent(): WorkplacesComponent {
        return DaggerWorkplacesComponent
            .factory()
            .create(requireContext())
    }

    private fun initToolbar() {
        with(binding) {
            workplacesToolbar.apply {
                setToolBarTitle(getString(R.string.toolbar_title_workplaces))
                setVisibilityToggleButton(false)
            }
        }
    }
}