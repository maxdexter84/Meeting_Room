package com.meetingroom.andersen.feature_landing.room_picker_dialog.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_landing.databinding.RoomAndTimePickerFragmentBinding
import com.meetingroom.andersen.feature_landing.di.room_picker_fragment.DaggerRoomPickerComponent
import com.meetingroom.andersen.feature_landing.di.room_picker_fragment.RoomPickerModule
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
            .sharedPreferencesModule(SharedPreferencesModule(requireContext()))
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.gagRooms.observe(viewLifecycleOwner) {
            val alreadySelectedRoom =
                viewModel.getUserChosenRoom() ?: ""
            it.forEach { room ->
                roomAdapter.roomsAndTime += RoomAndTimePickerData(
                    room.roomName,
                    alreadySelectedRoom == room.roomName,
                    room.roomName == "Paris" || room.roomName == "London"
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
        viewModel.saveUserChosenRoom(roomName)
        viewModel.getUserChosenRoom()?.let {
            args.upcomingEvent.eventRoom = it
        }
        findNavController().navigate(
            RoomPickerDialogFragmentDirections.actionRoomPickerDialogFragmentToModifyUpcomingEventFragment2(
                args.upcomingEvent
            )
        )
    }
}