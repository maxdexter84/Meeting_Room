package com.andersen.feature_rooms_screen.presentation.dialog_rooms.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.presentation.di.DaggerRoomsEventComponent
import com.andersen.feature_rooms_screen.presentation.di.RoomsEventComponent
import com.andersen.feature_rooms_screen.presentation.dialog_rooms.model.RoomPickerData
import com.andersen.feature_rooms_screen.presentation.dialog_rooms.presentation.RoomsViewModel
import com.andersen.feature_rooms_screen.presentation.rooms_event_grid.RoomsEventGridFragment
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_rooms_screen.databinding.DialogFragmentRoomsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import javax.inject.Inject

class DialogRoomsFragment :
    BaseDialogFragment<DialogFragmentRoomsBinding>(DialogFragmentRoomsBinding::inflate), IHasComponent<RoomsEventComponent> {

    private val roomsAdapter by lazy { RoomsAdapter { saveRoom(it) } }
    private val args: DialogRoomsFragmentArgs by navArgs()
    private var eventRoom = "All rooms"

    @Inject
    lateinit var roomsViewModel: RoomsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.bindComponent(this).inject(this)
    }

    override fun getComponent(): RoomsEventComponent {
       return DaggerRoomsEventComponent.builder().build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roomsListObserver()
        initRecyclerView()
        isCancelable = false
        isCheckedRadioButton()
        binding.radioButtonAllRooms.setOnClickListener {
            eventRoom = ALL_ROOMS_IN_OFFICE
            saveRoom(eventRoom)
        }
    }

    private fun roomsListObserver() {
        lifecycleScope.launch {
            roomsViewModel.mutableRoomList.collectLatest { rooms ->
                filterRoomsByFloor(rooms)
            }
        }
    }

    private fun filterRoomsByFloor(rooms: List<Room>) {
        val roomsNow = rooms as MutableList
        var floor = 1
        while (roomsNow.isNotEmpty()) {
            val roomsForEachFloor = roomsNow.filter { it.floor == floor }
            if (roomsForEachFloor.isNotEmpty()) {
                addToRecycler(roomsForEachFloor, floor)
            }
            roomsNow.removeIf { it.floor == floor }
            floor++
        }
    }

    private fun addToRecycler(listRoom: List<Room>, floor: Int) {
        val floorName = when (floor % 100) {
            11, 12, 13 -> "${floor}th"
            else -> {
                when (floor % 10) {
                    1 -> "${floor}st"
                    2 -> "${floor}nd"
                    3 -> "${floor}d"
                    else -> "${floor}th"
                }
            }
        }

        val alreadySelectedRoom = args.userRoom
        roomsAdapter.rooms += RoomPickerData(
            "All rooms on the $floorName floor",
            alreadySelectedRoom == "All rooms on the $floorName floor",
            true
        )
        listRoom.forEach { room ->
            roomsAdapter.rooms += RoomPickerData(
                room.title,
                alreadySelectedRoom == room.title,
                false
            )
        }
    }

    private fun isCheckedRadioButton() {
        when (args.userRoom) {
            ALL_ROOMS_IN_OFFICE -> binding.radioButtonAllRooms.isChecked = IS_CHECKED
        }
    }

    private fun initRecyclerView() {
        with(binding.recyclerViewRooms1stFloor) {
            setHasFixedSize(true)
            adapter = roomsAdapter
        }
    }

    private fun saveRoom(roomPickerData: RoomPickerData) {
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

    companion object {
        const val IS_CHECKED = true
        const val ALL_ROOMS_IN_OFFICE = "All rooms in office"
    }
}