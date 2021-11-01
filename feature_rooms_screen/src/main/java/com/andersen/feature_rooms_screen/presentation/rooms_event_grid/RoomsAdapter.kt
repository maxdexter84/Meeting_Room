package com.andersen.feature_rooms_screen.presentation.rooms_event_grid

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.domain.entity.RoomEvent
import com.meetingroom.andersen.feature_rooms_screen.databinding.ItemRoomBinding

class RoomsAdapter :
    RecyclerView.Adapter<RoomsAdapter.RoomViewHolder>() {

    var roomList = emptyList<Room>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var eventList = emptyList<RoomEvent>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {


        if (roomList.isNotEmpty()) holder.bind(roomList[position], position, eventList)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRoomBinding.inflate(layoutInflater, parent, false)
        return RoomViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    class RoomViewHolder(
        private val binding: ItemRoomBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            room: Room, position: Int, eventList: List<RoomEvent>,
        ) {

            with(binding) {
                roomTextView.text = room.title
                lineUnderRoomView.setBackgroundColor(room.color)
            }
            eventList.filter {
                it.colorRoom == room.color
            }.toString()

            with(binding.eventRecyclerView) {
                layoutManager =
                    LinearLayoutManager(binding.eventRecyclerView.context, LinearLayoutManager.VERTICAL, false)
                adapter = EventAdapter(eventList = eventList.filter { it.colorRoom == room.color }, isStartMainListPosition = position == 0)
            }
        }
    }

}