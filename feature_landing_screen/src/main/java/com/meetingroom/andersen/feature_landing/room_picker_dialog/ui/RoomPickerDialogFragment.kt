package com.meetingroom.andersen.feature_landing.room_picker_dialog.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meetingroom.andersen.feature_landing.databinding.RoomPickerFragmentBinding
import com.meetingroom.andersen.feature_landing.di.room_picker_fragment.DaggerRoomPickerComponent
import com.meetingroom.andersen.feature_landing.di.room_picker_fragment.RoomPickerModule
import com.meetingroom.andersen.feature_landing.room_picker_dialog.model.RoomPickerData
import com.meetingroom.andersen.feature_landing.room_picker_dialog.presentation.RoomPickerViewModel
import javax.inject.Inject

class RoomPickerDialogFragment : DialogFragment() {

    private lateinit var binding: RoomPickerFragmentBinding
    private val roomAdapter by lazy { RoomPickerAdapter { saveRoom(it) } }
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RoomPickerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.gagRooms.observe(viewLifecycleOwner) {
            val alreadySelectedRoom =
                viewModel.getUserChosenRoom() ?: ""
            for (room in it) {
                roomAdapter.rooms += RoomPickerData(
                    room.roomName,
                    alreadySelectedRoom == room.roomName,
                    room.roomName == "Paris" || room.roomName == "London"
                )
            }
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        with(binding) {
            root.setHasFixedSize(true)
            root.adapter = roomAdapter
        }
    }

    private fun saveRoom(roomName: String) {
        roomAdapter.rooms.filter {
            it.room != roomName
        }.map {
            it.isSelected = false
        }
        roomAdapter.rooms.filter {
            it.room == roomName
        }.map {
            it.isSelected = true
        }
        viewModel.saveUserChosenRoom(roomName)
        if (viewModel.getUserChosenRoom() != null) {
            args.upcomingEvent.eventRoom = viewModel.getUserChosenRoom()!!
        }
        findNavController().navigate(
            RoomPickerDialogFragmentDirections.actionRoomPickerDialogFragmentToModifyUpcomingEventFragment2(
                args.upcomingEvent
            )
        )
    }

    //TODO: Закрыть диалог после секунды после выбора
}