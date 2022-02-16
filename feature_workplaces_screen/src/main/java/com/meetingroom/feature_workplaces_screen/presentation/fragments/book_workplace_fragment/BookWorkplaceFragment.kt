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
import com.meeringroom.ui.event_dialogs.dialog_floor_room_picker.model.FloorRoomData
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
    private var arrayOfRooms = arrayOf<FloorRoomData>()
    private var selectedFloor = DEFAULT_FLOOR

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
        initCheckBoxListener()
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

    private fun initCheckBoxListener() {
        binding.cbExternalMonitor.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setExternalMonitorState(isChecked)
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.whenStarted {
                viewModel.floorRoomList.collect {
                    checkNumberOfRooms(it)
                }
            }
        }

        getNavigationResult(KEY_RESULT_FLOOR)?.observe(viewLifecycleOwner) { result ->
            setUpFloorTV(result)
            getRoomsByFloor(result)
            selectedFloor = result
            arrayOfFloors.map {
                it.isSelected = it.floorName == result
            }
        }

        getNavigationResult(KEY_RESULT_ROOM)?.observe(viewLifecycleOwner) { result ->
            setUpRoomTV(result)
            arrayOfRooms.map {
                it.isSelected = it.roomName == result
            }
        }
    }

    private fun checkNumberOfFloors(list: List<FloorData>) {
        if (list.size == SINGLE_ITEM) {
            with(binding) {
                setUpFloorTV(list.first().floorName)
                tvFloorTitle.isClickable = false
                getRoomsByFloor(list.first().floorName)
            }
        } else {
            arrayOfFloors = list.toTypedArray()
            initListenerToFloorDialog()
            tvRoomTitle.isClickable = false
        }
    }

    private fun checkNumberOfRooms(list: List<FloorRoomData>) {
        if (list.isNotEmpty()) {
            tvRoomTitle.text = list.first().roomName
            if (list.size == SINGLE_ITEM) {
                tvRoomTitle.isClickable = false
            } else {
                arrayOfRooms = list.toTypedArray()
                initListenerToFloorRoomDialog()
            }
        }
    }

    private fun setUpFloorTV(floorName: String) {
        tvFloorTitle.text = floorName
        btnBookWorkplace.visibilityIf(true)
        if (tvFloorTitle.text.toString() != getString(R.string.select_a_floor_default_title)) {
            tvRoomTitle.isClickable = true
        }
    }

    private fun setUpRoomTV(roomName: String) {
        tvRoomTitle.text = roomName
    }

    private fun getRoomsByFloor(selectedFloor: String) {
        viewModel.getRoomList(selectedFloor)
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

    private fun initListenerToFloorRoomDialog() {
        binding.tvRoomTitle.onClick {
            findNavController().navigate(
                BookWorkplaceFragmentDirections
                    .actionBookWorkplaceFragmentToFloorRoomPickerDialogNavigation(
                        arrayOfRooms, selectedFloor
                    )
            )
        }
    }

    companion object {
        const val SINGLE_ITEM = 1
        const val KEY_RESULT_FLOOR = "KEY_RESULT_FLOOR"
        const val KEY_RESULT_ROOM = "KEY_RESULT_FLOOR_ROOM"
        const val DEFAULT_FLOOR = "Default"
    }
}