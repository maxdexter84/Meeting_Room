package com.andersen.feature_rooms_screen.presentation.rooms_event_grid.dialog_rooms

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.domain.entity.rooms_event_grid.RoomPickerData
import com.andersen.feature_rooms_screen.presentation.di.DaggerRoomsEventComponent
import com.andersen.feature_rooms_screen.presentation.rooms_event_grid.RoomsEventGridFragment
import com.andersen.feature_rooms_screen.presentation.RoomsEventViewModel
import com.example.core_module.state.State
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.DialogFragmentRoomsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.vponomarenko.injectionmanager.x.XInjectionManager
import javax.inject.Inject

class DialogRoomsFragment :
        BaseDialogFragment<DialogFragmentRoomsBinding>(DialogFragmentRoomsBinding::inflate) {

    private val roomsAdapter by lazy { RoomsForDialogAdapter { saveRoom(it) } }
    private val args: DialogRoomsFragmentArgs by navArgs()
    private lateinit var eventRoom: String
    private lateinit var roomsList: List<Room>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val roomsViewModel: RoomsEventViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.findComponent<DaggerRoomsEventComponent>().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roomsListObserver()
        loadingStateObserver()
        initRecyclerView()
        isCancelable = false
        isCheckedRadioButton()
        binding.radioButtonAllRooms.setOnClickListener {
            eventRoom = getString(R.string.all_rooms_in_office)
            saveRoom(eventRoom)
        }
    }

    private fun roomsListObserver() {
        lifecycleScope.launch {
            roomsViewModel.mutableRoomList.collectLatest {
                roomsList = it
            }
        }
    }

    private fun loadingStateObserver() {
        lifecycleScope.launch {
            roomsViewModel.mutableState.collectLatest {
                when (it) {
                    is State.Loading -> binding.progressBar.isVisible = true
                    else -> {
                        binding.progressBar.isVisible = false
                        if (roomsList.isNotEmpty()) {
                            filterRoomsByFloor(roomsList)
                        } else {
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }

    private fun filterRoomsByFloor(rooms: List<Room>) {
        val roomsNow = rooms.toMutableList()
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
            11, 12, 13 -> getString(R.string.end_for_other_floors, floor)
            else -> {
                when (floor % 10) {
                    1 -> getString(R.string.end_for_first_floor, floor)
                    2 -> getString(R.string.end_for_second_floor, floor)
                    3 -> getString(R.string.end_for_third_floor, floor)
                    else -> getString(R.string.end_for_other_floors, floor)
                }
            }
        }
        val selectedAllRoomsString = getString(R.string.all_rooms_on_the, floorName)
        val alreadySelectedRoom = args.userRoom
        binding.radioButtonAllRooms.isVisible = true
        roomsAdapter.rooms += RoomPickerData(
                selectedAllRoomsString,
                alreadySelectedRoom == selectedAllRoomsString,
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
        if (args.userRoom == getString(R.string.all_rooms_in_office)) {
            binding.radioButtonAllRooms.isChecked = true
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
}