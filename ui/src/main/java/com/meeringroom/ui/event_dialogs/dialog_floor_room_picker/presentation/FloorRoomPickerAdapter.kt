package com.meeringroom.ui.event_dialogs.dialog_floor_room_picker.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.meeringroom.ui.event_dialogs.dialog_floor_room_picker.model.FloorRoomData
import com.meeringroom.ui.view_utils.onClick
import com.meetingroom.ui.R
import com.meetingroom.ui.databinding.FloorRoomItemBinding

class FloorRoomPickerAdapter(private val onItemSelected: (FloorRoomData) -> Unit) :
    ListAdapter<FloorRoomData, FloorRoomPickerAdapter.ViewHolder>(Comparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            FloorRoomItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: FloorRoomItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(room: FloorRoomData) {
            with(binding.rbFloorRoomPicker) {
                text = itemView.resources.getString(
                    R.string.floor_item_title, room.roomName, room.numberOfPlaces
                )
                isSelected = room.isSelected
                isChecked = room.isSelected
                onClick {
                    onItemSelected(room)
                }
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<FloorRoomData>() {
        override fun areItemsTheSame(oldItem: FloorRoomData, newItem: FloorRoomData) =
            oldItem.roomName == newItem.roomName

        override fun areContentsTheSame(oldItem: FloorRoomData, newItem: FloorRoomData) =
            oldItem == newItem
    }
}