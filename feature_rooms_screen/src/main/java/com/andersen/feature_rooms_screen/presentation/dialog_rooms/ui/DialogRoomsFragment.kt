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

        with(binding){
            radioButtonAllRooms.setOnClickListener {
                eventRoom = ALL_ROOMS_IN_OFFICE
                saveRoom(eventRoom)
            }
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
        val roomsFirstFloor: MutableList<Room> = mutableListOf()
        val roomsSecondFloor: MutableList<Room> = mutableListOf()
        val roomsThirdFloor: MutableList<Room> = mutableListOf()
        val roomsFourthFloor: MutableList<Room> = mutableListOf()
        val roomsFifthFloor: MutableList<Room> = mutableListOf()
        val roomsWithoutFloor: MutableList<Room> = mutableListOf()
        var floors = 0
        rooms.forEach {
            when (it.floor) {
                1 -> {
                    roomsFirstFloor.add(it)
                    if (floors < 1) {
                        floors = 1
                    }
                }
                2 -> {
                    roomsSecondFloor.add(it)
                    if (floors < 2) {
                        floors = 2
                    }
                }
                3 -> {
                    roomsThirdFloor.add(it)
                    if (floors < 3) {
                        floors = 3
                    }
                }
                4 -> {
                    roomsFourthFloor.add(it)
                    if (floors < 4) {
                        floors = 4
                    }
                }
                5 -> {
                    roomsFifthFloor.add(it)
                    if (floors < 5) {
                        floors = 5
                    }
                }
                else -> roomsWithoutFloor.add(it)
            }
        }

        if (roomsFirstFloor.isNotEmpty()) {
            addToRecycler(roomsFirstFloor, 1)
        }

        if (roomsSecondFloor.isNotEmpty()) {
            addToRecycler(roomsSecondFloor, 2)
        }

        if (roomsThirdFloor.isNotEmpty()) {
            addToRecycler(roomsThirdFloor, 3)
        }

        if (roomsFourthFloor.isNotEmpty()) {
            addToRecycler(roomsFourthFloor, 4)
        }

        if (roomsFifthFloor.isNotEmpty()) {
            addToRecycler(roomsFifthFloor, 5)
        }

    }

    private fun addToRecycler(listRoom: List<Room>, floor: Int) {
        val alreadySelectedRoom = args.userRoom
        roomsAdapter.rooms += RoomPickerData(
            "All rooms on $floor floor",
            alreadySelectedRoom == "All rooms on $floor floor",
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