package com.andersen.feature_rooms_screen.presentation.dialog_rooms.ui

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.andersen.feature_rooms_screen.presentation.dialog_rooms.model.RoomPickerData
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.DialogRoomsAdapterItemBinding

class RoomsAdapter(private val onItemSelected: (RoomPickerData) -> Unit) :
    RecyclerView.Adapter<RoomsAdapter.RoomViewHolder>() {

    var rooms = arrayListOf<RoomPickerData>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            rooms.clear()
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        return RoomViewHolder(
            DialogRoomsAdapterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(rooms[position])
    }

    override fun getItemCount() = rooms.size

    inner class RoomViewHolder(private val binding: DialogRoomsAdapterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(roomPickerData: RoomPickerData) {
            with(binding) {
                roomName.text = roomPickerData.room
                root.setOnClickListener { userChoseRoom(roomPickerData) }
                roomRadioButton.setOnClickListener { userChoseRoom(roomPickerData) }
                roomRadioButton.isChecked = roomPickerData.isSelected
                roomName.setTextColor(
                    androidx.core.content.ContextCompat.getColor(
                        root.context,
                        R.color.event_title_text_colour
                    )
                )
                view.isVisible = roomPickerData.isAllRooms
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        private fun userChoseRoom(roomPickerData: RoomPickerData) {
            onItemSelected(roomPickerData)
            notifyDataSetChanged()
        }
    }
}