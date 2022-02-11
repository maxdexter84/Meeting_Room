package com.andersen.feature_rooms_screen.presentation.rooms_event_grid.multiple_room_grid

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.presentation.utils.EmptyRoomEventForGrid
import com.andersen.feature_rooms_screen.presentation.utils.RoomEventForGrid
import com.meetingroom.andersen.feature_rooms_screen.databinding.ItemEventsRoomBinding
import java.time.LocalTime

class MainEventAdapter(
    private val onClickEvent: (RoomEventForGrid) -> Unit,
    private val click: (Triple<LocalTime, LocalTime, Room>) -> Unit
) :
    RecyclerView.Adapter<MainEventAdapter.EventsRoomViewHolder>() {

    var roomList = emptyList<Room>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var emptyEventList = emptyList<EmptyRoomEventForGrid>()

    var eventList = emptyList<RoomEventForGrid>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: EventsRoomViewHolder, position: Int) {
        if (roomList.isNotEmpty()) holder.bind(
            roomList[position],
            eventList,
            emptyEventList,
            click,
            onClickEvent
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsRoomViewHolder {
        return EventsRoomViewHolder(
            ItemEventsRoomBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = roomList.size

    class EventsRoomViewHolder(
        private val binding: ItemEventsRoomBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            room: Room,
            eventList: List<RoomEventForGrid>,
            emptyEventList: List<EmptyRoomEventForGrid>,
            click: (Triple<LocalTime, LocalTime, Room>) -> Unit,
            onClickEvent: (RoomEventForGrid) -> Unit
        ) {
            with(binding.eventRecyclerView) {
                layoutManager =
                    LinearLayoutManager(
                        binding.eventRecyclerView.context,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                adapter =
                    InnerEventAdapter(
                        eventList = eventList,
                        emptyEventList = emptyEventList,
                        onEventClick = onClickEvent,
                        room = room
                    ) {
                        click.invoke(Triple(it.first, it.second, room))
                    }
            }
        }
    }

}