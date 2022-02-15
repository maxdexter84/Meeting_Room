package com.meeringroom.ui.event_dialogs.dialog_floor_room_picker.presentation

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.meeringroom.ui.event_dialogs.dialog_floor_room_picker.model.FloorRoomData
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meeringroom.ui.view_utils.setNavigationResult
import com.meetingroom.ui.R
import com.meetingroom.ui.databinding.FloorRoomPickerDialogFragmentBinding

class FloorRoomPickerDialogFragment : BaseDialogFragment<FloorRoomPickerDialogFragmentBinding>(
    FloorRoomPickerDialogFragmentBinding::inflate) {

    private val args: FloorRoomPickerDialogFragmentArgs by navArgs()
    private val floorRoomAdapter by lazy {
        FloorRoomPickerAdapter {
            chooseRoom(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initUi()
    }

    private fun initRecyclerView() {
        with(binding) {
            rvFloorRoomDialog.adapter = floorRoomAdapter
        }

        floorRoomAdapter.submitList(args.floorRooms.toList())
    }

    private fun initUi() {
        with(binding) {
            var totalNumber = 0
            args.floorRooms.map {
                totalNumber += it.numberOfPlaces
            }
            tvFloorRoomTitle.text = resources.getString(
                R.string.floor_room_dialog_title, args.selectedFloor, totalNumber
            )
        }
    }

    private fun chooseRoom(floorRoom: FloorRoomData) {
        setNavigationResult(KEY_RESULT_FLOOR_ROOM, floorRoom.roomName)
        findNavController().popBackStack()
    }

    companion object {
        const val KEY_RESULT_FLOOR_ROOM = "KEY_RESULT_FLOOR_ROOM"
    }
}