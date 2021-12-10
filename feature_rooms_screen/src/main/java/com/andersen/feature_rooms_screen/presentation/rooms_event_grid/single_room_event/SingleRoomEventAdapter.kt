package com.andersen.feature_rooms_screen.presentation.rooms_event_grid.single_room_event

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andersen.feature_rooms_screen.presentation.rooms_event_grid.multiple_room_grid.InnerEventAdapter
import com.andersen.feature_rooms_screen.presentation.utils.EmptyRoomEventForGrid
import com.andersen.feature_rooms_screen.presentation.utils.RoomEventForGrid
import com.meetingroom.andersen.feature_rooms_screen.databinding.ItemEventSingleRoomBinding
import com.meetingroom.andersen.feature_rooms_screen.databinding.ItemGagEmptyEventBinding

class SingleRoomEventAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var eventList = emptyList<RoomEventForGrid>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var emptyEventList = emptyList<EmptyRoomEventForGrid>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EMPTY_EVENT_VIEW_HOLDER_TYPE ->
                EmptyEventsViewHolder(
                    ItemGagEmptyEventBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                )
            else -> EventsRoomViewHolder(
                ItemEventSingleRoomBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            EMPTY_EVENT_VIEW_HOLDER_TYPE -> (holder as EmptyEventsViewHolder).bind(position)
            EVENT_VIEW_HOLDER_TYPE -> (holder as EventsRoomViewHolder).bind(position)
        }
    }

    override fun getItemCount() = eventList.size + emptyEventList.size

    override fun getItemViewType(position: Int): Int {
        return when {
            position % 2 == 0 -> EMPTY_EVENT_VIEW_HOLDER_TYPE
            else -> EVENT_VIEW_HOLDER_TYPE
        }
    }

    inner class EventsRoomViewHolder(private val binding: ItemEventSingleRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            with(binding.root) {
                when (position) {
                    1 -> {
                        setEventDataInItem(eventList[0], binding)
                        layoutParams.height = eventList[0].heightEventItem
                    }
                    else
                    -> {
                        setEventDataInItem(eventList[(position - 1) / 2], binding)
                        layoutParams.height = eventList[(position - 1) / 2].heightEventItem
                    }
                }
            }
        }
    }

    inner class EmptyEventsViewHolder(private val binding: ItemGagEmptyEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.root.layoutParams.height = emptyEventList[position / 2].heightEventItem
            binding.itemGagEmptyRoomEvent.apply {
                layoutParams.height =
                    emptyEventList[position / InnerEventAdapter.POSITION_DIVISION].heightEventItem
                startTimeRange =
                    emptyEventList[position / InnerEventAdapter.POSITION_DIVISION].startTime
                endTimeRange =
                    emptyEventList[position / InnerEventAdapter.POSITION_DIVISION].startTime
            }
        }
    }

    private fun setEventDataInItem(
        roomEvent: RoomEventForGrid,
        binding: ItemEventSingleRoomBinding
    ) {
        with(binding) {
            eventCityColourLineUpcoming.setBackgroundColor(roomEvent.colorRoom)
            nameOfBooker.text = roomEvent.userFullName
            roleOfBooker.text = roomEvent.userPosition
            eventTitleUpcoming.visibility = roomEvent.titleVisibility
            eventTitleUpcoming.text = roomEvent.title
        }
    }

    companion object {
        private const val EMPTY_EVENT_VIEW_HOLDER_TYPE = 1
        private const val EVENT_VIEW_HOLDER_TYPE = 2
    }

}