package com.andersen.feature_rooms_screen.presentation.rooms_event_grid

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.domain.entity.RoomEvent
import com.meetingroom.andersen.feature_rooms_screen.databinding.ItemEventsRoomBinding
import javax.inject.Inject

class MainEventAdapter @Inject constructor() :
    RecyclerView.Adapter<MainEventAdapter.EventsRoomViewHolder>() {

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

    override fun onBindViewHolder(holder: EventsRoomViewHolder, position: Int) {

        if (roomList.isNotEmpty()) holder.bind(roomList[position], position, eventList)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsRoomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemEventsRoomBinding.inflate(layoutInflater, parent, false)
        return EventsRoomViewHolder(binding)
    }

    override fun getItemCount() = roomList.size

    class EventsRoomViewHolder(
        private val binding: ItemEventsRoomBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            room: Room, position: Int, eventList: List<RoomEvent>,
        ) {
            with(binding.eventRecyclerView) {
                layoutManager =
                    LinearLayoutManager(binding.eventRecyclerView.context, LinearLayoutManager.VERTICAL, false)
                adapter = InnerEventAdapter(eventList = eventList.filter { it.colorRoom == room.color }, isStartMainListPosition = position == 0)
            }
        }
    }

}