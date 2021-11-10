package com.andersen.feature_rooms_screen.presentation.dialog_rooms

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

    private val roomAdapter by lazy { RoomsAdapter { saveRoom(it) } }
    private val args: DialogRoomsFragmentArgs by navArgs()
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
            gagRoomData.forEach { room ->
                roomAdapter.rooms += RoomPickerData(
                    room.roomName,
                    alreadySelectedRoom == room.roomName
                )
            }
        }
        initRecyclerView()
        isCancelable = false
    }

    private fun initRecyclerView() {
        with(binding.recyclerViewRooms) {
            setHasFixedSize(true)
            adapter = roomAdapter
        }
    }

    private fun saveRoom(roomPickerData: RoomPickerData) {
        roomsViewModel.changeSelected(roomAdapter.rooms, roomPickerData.room)
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            RoomsEventGridFragment.ROOM_KEY,
            roomPickerData.room
        )
        findNavController().popBackStack()
    }
}