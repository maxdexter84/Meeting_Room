package com.meeringroom.ui.event_dialogs.dialog_room_picker.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.meeringroom.ui.event_dialogs.dialog_room_picker.model.RoomPickerNewEventData
import com.meeringroom.ui.view.base_classes.BaseDialogFragment
import com.meetingroom.ui.databinding.RoomAndTimePickerFragmentBinding
import kotlinx.coroutines.launch

class RoomPickerDialogFragment :
    BaseDialogFragment<RoomAndTimePickerFragmentBinding>(RoomAndTimePickerFragmentBinding::inflate) {

    private val args: RoomPickerDialogFragmentArgs by navArgs()
    private val roomAdapter by lazy { RoomPickerAdapter { saveRoom(it) }.apply {
        rooms = args.rooms.toList()
        rooms.findLast { it.room == args.userRoom }?.isSelected = true
    }}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        roomAdapter.rooms.forEach { it.isSelected = false }
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            args.key,
            roomPickerData.room
        )
        findNavController().popBackStack()
    }
}
