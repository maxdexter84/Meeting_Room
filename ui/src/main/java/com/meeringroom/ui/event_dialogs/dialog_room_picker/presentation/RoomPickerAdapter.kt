package com.meeringroom.ui.event_dialogs.dialog_room_picker.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.meeringroom.ui.event_dialogs.dialog_room_picker.model.RoomPickerNewEventData
import com.meetingroom.ui.R
import com.meetingroom.ui.databinding.RoomAndTimeAdapterItemBinding

class RoomPickerAdapter(private val onItemSelected: (RoomPickerNewEventData) -> Unit) :
    RecyclerView.Adapter<RoomPickerAdapter.RoomViewHolder>() {

    var rooms = listOf<RoomPickerNewEventData>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
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

        fun bind(roomPickerData: RoomPickerNewEventData) {
            with(binding) {
                roomAndTimeName.text = roomPickerData.titleRoom
                if (roomPickerData.isEnabled.not()) {
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
                roomAndTimeRadioButton.isEnabled = roomPickerData.isEnabled
                roomAndTimeRadioButton.isClickable = roomPickerData.isEnabled
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        private fun userChoseRoom(roomPickerData: RoomPickerNewEventData) {
            onItemSelected(roomPickerData)
            notifyDataSetChanged()
        }
    }
}
