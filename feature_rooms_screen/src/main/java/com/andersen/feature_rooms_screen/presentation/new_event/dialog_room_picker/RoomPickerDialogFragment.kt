package com.andersen.feature_rooms_screen.presentation.new_event.dialog_room_picker

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andersen.feature_rooms_screen.domain.entity.new_event.RoomPickerNewEventData
import com.andersen.feature_rooms_screen.presentation.di.DaggerRoomsEventComponent
import com.andersen.feature_rooms_screen.presentation.RoomsEventViewModel
import com.andersen.feature_rooms_screen.presentation.new_event.NewEventFragment
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.ui.databinding.RoomAndTimePickerFragmentBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.vponomarenko.injectionmanager.x.XInjectionManager
import javax.inject.Inject

class RoomPickerDialogFragment :
    BaseDialogFragment<RoomAndTimePickerFragmentBinding>(RoomAndTimePickerFragmentBinding::inflate) {

    private val roomAdapter by lazy { RoomPickerAdapter { saveRoom(it) } }
    private val args: RoomPickerDialogFragmentArgs by navArgs()

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
        lifecycleScope.launch {
            roomsViewModel.mutableRoomList.collectLatest { it ->
                val freeRooms = roomsViewModel.getFreeRoomsList()
                val roomsList = arrayListOf<RoomPickerNewEventData>()
                if (it.isNotEmpty()) {
                    it.forEach { room ->
                        roomsList.add(
                            RoomPickerNewEventData(
                                room.title,
                                args.userRoom == room.title,
                                room in freeRooms
                            )
                        )
                    }
                    roomAdapter.rooms =
                        roomsList.sortedByDescending { room -> room.isEnabled }
                }
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

    private fun saveRoom(roomPickerData: RoomPickerNewEventData) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            NewEventFragment.ROOM_KEY,
            roomPickerData.room
        )
        findNavController().popBackStack()
    }
}
