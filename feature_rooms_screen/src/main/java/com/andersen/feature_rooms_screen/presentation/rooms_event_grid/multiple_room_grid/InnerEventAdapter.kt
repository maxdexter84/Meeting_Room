package com.andersen.feature_rooms_screen.presentation.rooms_event_grid.multiple_room_grid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andersen.feature_rooms_screen.presentation.utils.EmptyRoomEventForGrid
import com.andersen.feature_rooms_screen.presentation.utils.RoomEventForGrid
import com.meetingroom.andersen.feature_rooms_screen.databinding.ItemEventBinding
import com.meetingroom.andersen.feature_rooms_screen.databinding.ItemGagEmptyEventBinding

class InnerEventAdapter(
    val eventList: List<RoomEventForGrid>,
    val emptyEventList: List<EmptyRoomEventForGrid>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EMPTY_EVENT_VIEW_HOLDER_TYPE ->
                EmptyEventsViewHolder(
                    ItemGagEmptyEventBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                )
            else -> EventsRoomViewHolder(
                ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class EventsRoomViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            with(binding) {
                when (position) {
                    1 -> {
                        root.layoutParams.height = eventList[0].heightEventItem
                        eventView.isUserOwnEvent = eventList[0].isUserOwnEvent
                        eventView.colorEvent = eventList[0].colorRoom
                    }
                    else
                    -> {
                        root.layoutParams.height = eventList[(position - 1) / 2].heightEventItem
                        eventView.isUserOwnEvent = eventList[(position - 1) / 2].isUserOwnEvent
                        eventView.colorEvent = eventList[(position - 1) / 2].colorRoom
                    }
                }
            }
        }
    }

    inner class EmptyEventsViewHolder(private val binding: ItemGagEmptyEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.itemGagEmptyRoomEvent.apply {
                layoutParams.height = emptyEventList[position / POSITION_DIVISION].heightEventItem
                startTimeRange = emptyEventList[position / POSITION_DIVISION].startTime
                endTimeRange = emptyEventList[position / POSITION_DIVISION].startTime
            }

        }
    }

    companion object {
        private const val EMPTY_EVENT_VIEW_HOLDER_TYPE = 1
        private const val EVENT_VIEW_HOLDER_TYPE = 2
        const val POSITION_DIVISION = 2

    }
}
