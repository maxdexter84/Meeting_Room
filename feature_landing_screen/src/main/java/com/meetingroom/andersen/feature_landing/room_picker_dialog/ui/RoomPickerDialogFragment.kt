package com.meetingroom.andersen.feature_landing.room_picker_dialog.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_landing.databinding.RoomAndTimePickerFragmentBinding
import com.meetingroom.andersen.feature_landing.di.room_picker_fragment.DaggerRoomPickerComponent
import com.meetingroom.andersen.feature_landing.di.room_picker_fragment.RoomPickerModule
import com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.ui.ModifyUpcomingEventFragment
import com.meetingroom.andersen.feature_landing.room_picker_dialog.model.RoomAndTimePickerData
import com.meetingroom.andersen.feature_landing.room_picker_dialog.presentation.RoomPickerViewModel
import javax.inject.Inject

class RoomPickerDialogFragment :
    BaseDialogFragment<RoomAndTimePickerFragmentBinding>(RoomAndTimePickerFragmentBinding::inflate) {

    private val roomAdapter by lazy { RoomAndTimePickerAdapter { saveRoom(it) } }
    private val args: RoomPickerDialogFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel: RoomPickerViewModel

    override fun onAttach(context: Context) {
        DaggerRoomPickerComponent.builder()
            .roomPickerModule(RoomPickerModule(this))
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.gagRooms.observe(viewLifecycleOwner) { gagRoomData ->
            val alreadySelectedRoom = args.userRoom
            gagRoomData.forEach { room ->
                roomAdapter.roomsAndTime += RoomAndTimePickerData(
                    room.roomName,
                    alreadySelectedRoom == room.roomName,
                    room.isBusy
                )
            }
        }
        initRecyclerView()
        isCancelable = false
    }

    private fun initRecyclerView() {
        with(binding) {
            root.setHasFixedSize(true)
            root.adapter = roomAdapter
        }
    }

    private fun saveRoom(roomName: String) {
        viewModel.changeSelected(roomAdapter.roomsAndTime, roomName)
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            ModifyUpcomingEventFragment.ROOM_KEY,
            roomName
        )
        findNavController().popBackStack()
    }
}