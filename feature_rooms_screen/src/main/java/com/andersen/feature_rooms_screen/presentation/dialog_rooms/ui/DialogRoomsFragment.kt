package com.andersen.feature_rooms_screen.presentation.dialog_rooms.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andersen.feature_rooms_screen.presentation.di.rooms_dialog.DaggerRoomsComponent
import com.andersen.feature_rooms_screen.presentation.di.rooms_dialog.RoomsModule
import com.andersen.feature_rooms_screen.presentation.dialog_rooms.model.RoomPickerData
import com.andersen.feature_rooms_screen.presentation.dialog_rooms.presentation.RoomsViewModel
import com.andersen.feature_rooms_screen.presentation.rooms_event_grid.RoomsEventGridFragment
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_rooms_screen.databinding.DialogFragmentRoomsBinding
import javax.inject.Inject

class DialogRoomsFragment :
    BaseDialogFragment<DialogFragmentRoomsBinding>(DialogFragmentRoomsBinding::inflate) {

    private val roomAdapter1 by lazy { RoomsAdapter { saveRoom(it, 1) } }
    private val roomAdapter2 by lazy { RoomsAdapter { saveRoom(it, 2) } }
    private val args: DialogRoomsFragmentArgs by navArgs()
    private var eventRoom = "All rooms"

    @Inject
    lateinit var roomsViewModel: RoomsViewModel

    override fun onAttach(context: Context) {
        DaggerRoomsComponent.builder()
            .roomsModule(RoomsModule(this))
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roomsViewModel.gagRooms.observe(viewLifecycleOwner) { gagRoomData ->
            val alreadySelectedRoom = args.userRoom
            val gagRooms1 = gagRoomData.filter { it.floor == 1 }
            val gagRooms2 = gagRoomData.filter { it.floor == 2 }
            gagRooms1.forEach { room ->
                roomAdapter1.rooms += RoomPickerData(
                    room.roomName,
                    alreadySelectedRoom == room.roomName
                )
            }
            gagRooms2.forEach { room ->
                roomAdapter2.rooms += RoomPickerData(
                    room.roomName,
                    alreadySelectedRoom == room.roomName
                )
            }
        }
        initRecyclerView1()
        initRecyclerView2()
        isCancelable = false
        isCheckedRadioButton()

        with(binding){
            radioButtonAllRooms.setOnClickListener {
                eventRoom = "All rooms in office"
                saveRoom(eventRoom)
            }
            radioButtonAllRoomsOn1stFloor.setOnClickListener {
                eventRoom = "All rooms on 1st floor"
                saveRoom(eventRoom)
            }
            radioButtonAllRoomsOn2ndFloor.setOnClickListener {
                eventRoom = "All rooms on 2nd floor"
                saveRoom(eventRoom)
            }
        }
    }

    private fun isCheckedRadioButton(){
        when(args.userRoom){
            "All rooms in office" -> binding.radioButtonAllRooms.isChecked = true
            "All rooms on 1st floor" -> binding.radioButtonAllRoomsOn1stFloor.isChecked = true
            "All rooms on 2nd floor" -> binding.radioButtonAllRoomsOn2ndFloor.isChecked = true
        }
    }

    private fun initRecyclerView1() {
        with(binding.recyclerViewRooms1stFloor) {
            setHasFixedSize(true)
            adapter = roomAdapter1
        }
    }

    private fun initRecyclerView2() {
        with(binding.recyclerViewRooms2ndFloor) {
            setHasFixedSize(true)
            adapter = roomAdapter2
        }
    }

    private fun saveRoom(roomPickerData: RoomPickerData, floor: Int) {
        if (floor == 1) {
            roomsViewModel.changeSelected(roomAdapter1.rooms, roomPickerData.room)
        } else {
            roomsViewModel.changeSelected(roomAdapter2.rooms, roomPickerData.room)
        }
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            RoomsEventGridFragment.ROOM_KEY,
            roomPickerData.room
        )
        findNavController().popBackStack()
    }

    private fun saveRoom(room: String) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            RoomsEventGridFragment.ROOM_KEY,
            room
        )
        findNavController().popBackStack()
    }
}