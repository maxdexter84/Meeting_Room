package com.meetingroom.feature_workplaces_screen.presentation.fragments.book_workplace_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.navigation.fragment.findNavController
import com.example.core_module.component_manager.XInjectionManager
import com.meeringroom.ui.event_dialogs.dialog_floor_picker.model.FloorData
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view_utils.getNavigationResult
import com.meeringroom.ui.view_utils.onClick
import com.meeringroom.ui.view_utils.visibilityIf
import com.meetingroom.feature_workplaces_screen.R
import com.meetingroom.feature_workplaces_screen.databinding.FragmentBookWorkplaceBinding
import com.meetingroom.feature_workplaces_screen.presentation.di.WorkplacesComponent
import kotlinx.android.synthetic.main.fragment_book_workplace.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class BookWorkplaceFragment :
    BaseFragment<FragmentBookWorkplaceBinding>(FragmentBookWorkplaceBinding::inflate) {

    private var arrayOfFloors = arrayOf<FloorData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: BookWorkplaceViewModel by viewModels {
        viewModelFactory
    }

    private fun injectDependencies() {
        XInjectionManager.findComponent<WorkplacesComponent>()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initObservers()
    }

    private fun initToolbar() {
        with(binding.toolbarBookWorkplace) {
            tvToolbarTitle.text =
                getText(R.string.toolbar_title_book_workplace)

            ivUpButton.setOnClickListener {
                findNavController().navigate(
                    BookWorkplaceFragmentDirections
                        .actionBookWorkplaceFragmentToWorkplacesFragment()
                )
            }
        }
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.whenStarted {
                viewModel.officeList.collect {
                    checkNumberOfFloors(it)
                }
            }
        }

        getNavigationResult(KEY_RESULT_FLOOR)?.observe(viewLifecycleOwner) { result ->
            setViewElementsUp(result)
            arrayOfFloors.map {
                it.isSelected = it.floorName == result
            }
        }
    }

    private fun checkNumberOfFloors(list: List<FloorData>) {
        if (list.size == SINGLE_FLOOR) {
            with(binding) {
                setViewElementsUp(list.first().floorName)
                tvFloorTitle.isClickable = false
            }
        } else {
            arrayOfFloors = list.toTypedArray()
            initListenerToFloorDialog()
            tvRoomTitle.isClickable = false
        }
    }

    private fun setViewElementsUp(floorName: String) {
        tvFloorTitle.text = floorName
        btnBookWorkplace.visibilityIf(true)
        if (tvFloorTitle.text.toString() != getString(R.string.select_a_floor_default_title)) {
            tvRoomTitle.isClickable = true
        }
    }

    private fun initListenerToFloorDialog() {
        binding.tvFloorTitle.onClick {
            findNavController().navigate(
                BookWorkplaceFragmentDirections
                    .actionBookWorkplaceFragmentToFloorPickerDialogNavigation(
                        arrayOfFloors
                    )
            )
        }
    }

    companion object {
        const val SINGLE_FLOOR = 1
        const val KEY_RESULT_FLOOR = "key result floor"
    }
}