package com.andersen.feature_rooms_screen.presentation.rooms_event_grid

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.domain.entity.RoomEvent
import com.meetingroom.andersen.feature_rooms_screen.databinding.ItemRoomBinding
import javax.inject.Inject

class RoomsAdapter @Inject constructor() :
    RecyclerView.Adapter<RoomsAdapter.RoomViewHolder>() {

    var roomList = emptyList<Room>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {

        if (roomList.isNotEmpty()) holder.bind(roomList[position])

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRoomBinding.inflate(layoutInflater, parent, false)
        return RoomViewHolder(binding)
    }

    override fun getItemCount() = roomList.size

    class RoomViewHolder(
        private val binding: ItemRoomBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            room: Room
        ) {

            with(binding) {
                roomTextView.text = room.title
                lineUnderRoomView.setBackgroundColor(room.color)
            }
        }
    }

}