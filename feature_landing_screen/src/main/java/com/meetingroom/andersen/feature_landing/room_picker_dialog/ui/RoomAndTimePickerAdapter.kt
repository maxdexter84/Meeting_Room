package com.meetingroom.andersen.feature_landing.room_picker_dialog.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.RoomAndTimeAdapterItemBinding
import com.meetingroom.andersen.feature_landing.room_picker_dialog.model.RoomAndTimePickerData

class RoomAndTimePickerAdapter(private val onItemSelected: (String) -> Unit) :
    RecyclerView.Adapter<RoomAndTimePickerAdapter.RoomAndTimePickerViewHolder>() {


    var roomsAndTime = arrayListOf<RoomAndTimePickerData>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            roomsAndTime.clear()
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomAndTimePickerViewHolder {
        return RoomAndTimePickerViewHolder(
            RoomAndTimeAdapterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holderAndTime: RoomAndTimePickerViewHolder, position: Int) {
        holderAndTime.bind(roomsAndTime[position])
    }

    override fun getItemCount() = roomsAndTime.size

    inner class RoomAndTimePickerViewHolder(private val binding: RoomAndTimeAdapterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(roomAndTimePickerData: RoomAndTimePickerData) {
            with(binding) {
                roomAndTimeName.text = roomAndTimePickerData.roomAndTime
                if (roomAndTimePickerData.isEnabled) {
                    roomAndTimeName.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.grey_text_color
                        )
                    )
                } else {
                    root.setOnClickListener { userChoseRoom(roomAndTimePickerData) }
                    roomAndTimeRadioButton.setOnClickListener { userChoseRoom(roomAndTimePickerData) }
                    roomAndTimeRadioButton.isChecked = roomAndTimePickerData.isSelected
                    roomAndTimeName.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.event_title_text_colour
                        )
                    )
                }
                roomAndTimeRadioButton.isEnabled = !roomAndTimePickerData.isEnabled
                roomAndTimeRadioButton.isClickable = !roomAndTimePickerData.isEnabled
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        private fun userChoseRoom(roomAndTimePickerData: RoomAndTimePickerData) {
            onItemSelected(roomAndTimePickerData.roomAndTime)
            notifyDataSetChanged()
        }
    }
}