package com.andersen.feature_rooms_screen.presentation.dialog_rooms.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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

    private val roomAdapterFirstFloor by lazy { RoomsAdapter { saveRoom(it, FIRST_FLOOR) } }
    private val roomAdapterSecondFloor by lazy { RoomsAdapter { saveRoom(it, SECOND_FLOOR) } }
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
        bindViewModel()
        initRecyclerViewFirstFloor()
        initRecyclerViewSecondFloor()
        isCancelable = false
        isCheckedRadioButton()

        with(binding){
            radioButtonAllRooms.setOnClickListener {
                eventRoom = ALL_ROOMS_IN_OFFICE
                saveRoom(eventRoom)
            }
            radioButtonAllRoomsOn1stFloor.setOnClickListener {
                eventRoom = ALL_ROOMS_ON_FIRST_FLOOR
                saveRoom(eventRoom)
            }
            radioButtonAllRoomsOn2ndFloor.setOnClickListener {
                eventRoom = ALL_ROOMS_ON_SECOND_FLOOR
                saveRoom(eventRoom)
            }
        }
    }

    private fun bindViewModel(){
        roomsViewModel.gagRooms.observe(viewLifecycleOwner) { room ->
            val alreadySelectedRoom = args.userRoom
            val roomsFirstFloor = room.filter { it.floor == FIRST_FLOOR }
            val roomsSecondFloor = room.filter { it.floor == SECOND_FLOOR }
            roomsFirstFloor.forEach { room ->
                roomAdapterFirstFloor.rooms += RoomPickerData(
                    room.title,
                    alreadySelectedRoom == room.title
                )
            }
            roomsSecondFloor.forEach { room ->
                roomAdapterSecondFloor.rooms += RoomPickerData(
                    room.title,
                    alreadySelectedRoom == room.title
                )
            }
        }
    }

    private fun isCheckedRadioButton(){
        when(args.userRoom){
            ALL_ROOMS_IN_OFFICE -> binding.radioButtonAllRooms.isChecked = IS_CHECKED
            ALL_ROOMS_ON_FIRST_FLOOR -> binding.radioButtonAllRoomsOn1stFloor.isChecked = IS_CHECKED
            ALL_ROOMS_ON_SECOND_FLOOR -> binding.radioButtonAllRoomsOn2ndFloor.isChecked = IS_CHECKED
        }
    }

    private fun initRecyclerViewFirstFloor() {
        with(binding.recyclerViewRooms1stFloor) {
            setHasFixedSize(true)
            adapter = roomAdapterFirstFloor
        }
    }

    private fun initRecyclerViewSecondFloor() {
        with(binding.recyclerViewRooms2ndFloor) {
            setHasFixedSize(true)
            adapter = roomAdapterSecondFloor
        }
    }

    private fun saveRoom(roomPickerData: RoomPickerData, floor: Int) {
        if (floor == FIRST_FLOOR) {
            roomsViewModel.changeSelected(roomAdapterFirstFloor.rooms, roomPickerData.room)
        } else {
            roomsViewModel.changeSelected(roomAdapterSecondFloor.rooms, roomPickerData.room)
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

    companion object {
        const val IS_CHECKED = true
        const val FIRST_FLOOR = 1
        const val SECOND_FLOOR = 2
        const val ALL_ROOMS_IN_OFFICE = "All rooms in office"
        const val ALL_ROOMS_ON_FIRST_FLOOR = "All rooms on 1st floor"
        const val ALL_ROOMS_ON_SECOND_FLOOR = "All rooms on 2nd floor"
    }
}