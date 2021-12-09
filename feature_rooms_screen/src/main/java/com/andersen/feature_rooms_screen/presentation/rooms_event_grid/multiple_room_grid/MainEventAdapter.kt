package com.andersen.feature_rooms_screen.presentation.rooms_event_grid.multiple_room_grid

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.presentation.rooms_event_grid.RoomsEventGridFragmentDirections
import com.andersen.feature_rooms_screen.presentation.utils.EmptyRoomEventForGrid
import com.andersen.feature_rooms_screen.presentation.utils.RoomEventForGrid
import com.meetingroom.andersen.feature_rooms_screen.databinding.ItemEventsRoomBinding
import java.time.LocalDate
import javax.inject.Inject

class MainEventAdapter @Inject constructor(val click:()->Unit) :
    RecyclerView.Adapter<MainEventAdapter.EventsRoomViewHolder>() {

    var roomList = emptyList<Room>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var emptyEventList  = emptyList<EmptyRoomEventForGrid>()

    var eventList = emptyList<RoomEventForGrid>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: EventsRoomViewHolder, position: Int) {
        if (roomList.isNotEmpty()) holder.bind(roomList[position], position, eventList, emptyEventList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsRoomViewHolder {
        return EventsRoomViewHolder(ItemEventsRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = roomList.size

    class EventsRoomViewHolder(
        private val binding: ItemEventsRoomBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            room: Room, position: Int,
            eventList: List<RoomEventForGrid>,
            emptyEventList: List<EmptyRoomEventForGrid>,
        ) {
            with(binding.eventRecyclerView) {
                layoutManager =
                    LinearLayoutManager(binding.eventRecyclerView.context, LinearLayoutManager.VERTICAL, false)
                adapter = InnerEventAdapter(eventList = eventList, emptyEventList = emptyEventList) {
                    findNavController().navigate(
                        RoomsEventGridFragmentDirections.actionRoomsFragmentToNewEventFragment(
                            LocalDate.now(), "Yellow"
                        )
                    )
                }
            }
        }
    }

}