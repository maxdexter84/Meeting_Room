package com.meetingroom.andersen.feature_landing.room_picker_dialog.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.RoomAndTimeAdapterItemBinding
import com.meetingroom.andersen.feature_landing.room_picker_dialog.model.RoomPickerData

class RoomPickerAdapter(private val onItemSelected: (RoomPickerData) -> Unit) :
    RecyclerView.Adapter<RoomPickerAdapter.RoomViewHolder>() {

    var rooms = arrayListOf<RoomPickerData>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            rooms.clear()
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        return RoomViewHolder(
            RoomAndTimeAdapterItemBinding.inflate(
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

    inner class RoomViewHolder(private val binding: RoomAndTimeAdapterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(roomPickerData: RoomPickerData) {
            with(binding) {
                roomAndTimeName.text = roomPickerData.room
                if (roomPickerData.isEnabled) {
                    roomAndTimeName.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.grey_text_color
                        )
                    )
                } else {
                    root.setOnClickListener { userChoseRoom(roomPickerData) }
                    roomAndTimeRadioButton.setOnClickListener { userChoseRoom(roomPickerData) }
                    roomAndTimeRadioButton.isChecked = roomPickerData.isSelected
                    roomAndTimeName.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.event_title_text_colour
                        )
                    )
                }
                roomAndTimeRadioButton.isEnabled = !roomPickerData.isEnabled
                roomAndTimeRadioButton.isClickable = !roomPickerData.isEnabled
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        private fun userChoseRoom(roomPickerData: RoomPickerData) {
            onItemSelected(roomPickerData)
            notifyDataSetChanged()
        }
    }
}