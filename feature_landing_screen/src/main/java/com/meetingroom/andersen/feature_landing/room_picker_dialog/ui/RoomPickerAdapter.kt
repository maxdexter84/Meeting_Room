package com.meetingroom.andersen.feature_landing.room_picker_dialog.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.RoomAdapterItemBinding
import com.meetingroom.andersen.feature_landing.room_picker_dialog.model.RoomPickerData

class RoomPickerAdapter(private val onItemSelected: (String) -> Unit) :
    RecyclerView.Adapter<RoomPickerAdapter.RoomPickerViewHolder>() {


    var rooms = arrayListOf<RoomPickerData>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            rooms.clear()
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomPickerViewHolder {
        return RoomPickerViewHolder(
            RoomAdapterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RoomPickerViewHolder, position: Int) {
        holder.bind(rooms[position])
    }

    override fun getItemCount() = rooms.size

    inner class RoomPickerViewHolder(private val binding: RoomAdapterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(roomPickerData: RoomPickerData) {
            with(binding) {
                roomName.text = roomPickerData.room
                if (roomPickerData.isEnabled) {
                    roomName.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.grey_text_color
                        )
                    )
                    roomRadioButton.isEnabled = false
                    roomRadioButton.isClickable = false
                } else {
                    root.setOnClickListener { userChoseRoom(roomPickerData) }
                    roomRadioButton.setOnClickListener { userChoseRoom(roomPickerData) }
                    roomRadioButton.isChecked = roomPickerData.isSelected
                    roomName.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.event_title_text_colour
                        )
                    )
                    roomRadioButton.isEnabled = true
                    roomRadioButton.isClickable = true
                }
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        private fun userChoseRoom(roomPickerData: RoomPickerData) {
            onItemSelected(roomPickerData.room)
            notifyDataSetChanged()
        }
    }
}