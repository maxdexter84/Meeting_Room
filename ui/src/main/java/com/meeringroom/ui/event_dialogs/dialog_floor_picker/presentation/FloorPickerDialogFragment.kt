package com.meeringroom.ui.event_dialogs.dialog_floor_picker.presentation

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.meeringroom.ui.event_dialogs.dialog_floor_picker.model.FloorData
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meeringroom.ui.view_utils.setNavigationResult
import com.meetingroom.ui.databinding.FloorPickerDialogFragmentBinding

class FloorPickerDialogFragment : BaseDialogFragment<FloorPickerDialogFragmentBinding>(
    FloorPickerDialogFragmentBinding::inflate) {

    private val args: FloorPickerDialogFragmentArgs by navArgs()
    private val floorAdapter by lazy {
        FloorPickerAdapter {
            chooseFloor(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        with(binding) {
            root.adapter = floorAdapter
        }

        floorAdapter.submitList(args.floors.toList())
    }

    private fun chooseFloor(floor: FloorData) {
        setNavigationResult(KEY_RESULT_FLOOR, floor.floorName)
        findNavController().popBackStack()
    }

    companion object {
        const val KEY_RESULT_FLOOR = "KEY_RESULT_FLOOR"
    }
}