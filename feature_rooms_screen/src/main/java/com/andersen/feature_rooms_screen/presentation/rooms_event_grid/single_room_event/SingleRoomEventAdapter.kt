package com.andersen.feature_rooms_screen.presentation.rooms_event_grid.single_room_event

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andersen.feature_rooms_screen.domain.entity.RoomEvent
import com.example.core_module.utils.TimeUtilsConstants.END_WORK_TIME_IN_OFFICE
import com.example.core_module.utils.TimeUtilsConstants.OFFICE_WORKING_TIME_IN_MINUTES
import com.example.core_module.utils.TimeUtilsConstants.START_WORK_TIME_IN_OFFICE
import com.example.core_module.utils.TimeUtilsConstants.TIME_DATE_FORMAT
import com.example.core_module.utils.TimeUtilsConstants.TIME_FORMAT
import com.example.core_module.utils.stringToTime
import com.meetingroom.andersen.feature_rooms_screen.databinding.ItemEventSingleRoomBinding
import com.meetingroom.andersen.feature_rooms_screen.databinding.ItemGagEmptyEventBinding
import java.time.Duration
import javax.inject.Inject

class SingleRoomEventAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var eventList = emptyList<RoomEvent>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var heightSingleRoomGrid = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EMPTY_EVENT_VIEW_HOLDER_TYPE ->
                EmptyEventsViewHolder(
                    ItemGagEmptyEventBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                )
            else -> EventsRoomViewHolder(
                ItemEventSingleRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            EMPTY_EVENT_VIEW_HOLDER_TYPE -> (holder as EmptyEventsViewHolder).bind(position)
            EVENT_VIEW_HOLDER_TYPE -> (holder as EventsRoomViewHolder).bind(position)
        }
    }

    override fun getItemCount(): Int {
        val emptyEventsRoomSize = eventList.size + 1
        return eventList.size + emptyEventsRoomSize
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position % 2 == 0 -> EMPTY_EVENT_VIEW_HOLDER_TYPE
            else -> EVENT_VIEW_HOLDER_TYPE
        }
    }

    inner class EventsRoomViewHolder(val binding: ItemEventSingleRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            if (eventList.isNotEmpty()) {
                with(binding.root) {
                    when (position) {
                        1 -> {
                            val minuteInterval = Duration.between(
                                eventList[0].startDateTime.stringToTime(TIME_DATE_FORMAT),
                                eventList[0].endDateTime.stringToTime(TIME_DATE_FORMAT)
                            ).toMinutes()
                            setEventDataInItem(eventList[0], binding, minuteInterval)
                            layoutParams.height = calculateHeightEventItem(minuteInterval)

                        }
                        else
                        -> {
                            val minuteInterval = Duration.between(
                                eventList[(position - 1) / 2].startDateTime.stringToTime(TIME_DATE_FORMAT),
                                eventList[(position - 1) / 2].endDateTime.stringToTime(TIME_DATE_FORMAT)
                            ).toMinutes()
                            setEventDataInItem(eventList[(position - 1) / 2], binding, minuteInterval)
                            layoutParams.height = calculateHeightEventItem(minuteInterval)
                        }
                    }
                }
            }
        }
    }

    inner class EmptyEventsViewHolder(val binding: ItemGagEmptyEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            with(binding.root) {
                if (eventList.isEmpty()) layoutParams.height = heightSingleRoomGrid
                else {
                    when (position) {
                        0 -> {
                            layoutParams.height =
                                calculateHeightEventItem(
                                    Duration.between(
                                        START_WORK_TIME_IN_OFFICE.stringToTime(TIME_FORMAT),
                                        eventList[0].endDateTime.stringToTime(TIME_DATE_FORMAT)
                                    ).toMinutes()
                                )
                        }
                        itemCount - 1 -> {
                            layoutParams.height =
                                calculateHeightEventItem(
                                    Duration.between(
                                        eventList.last().endDateTime.stringToTime(TIME_DATE_FORMAT),
                                        END_WORK_TIME_IN_OFFICE.stringToTime(TIME_FORMAT)
                                    ).toMinutes()
                                )
                        }
                        else -> {
                            layoutParams.height =
                                calculateHeightEventItem(
                                    Duration.between(
                                        eventList[(position - 1) / 2].endDateTime.stringToTime(TIME_DATE_FORMAT),
                                        eventList[(position + 1) / 2].startDateTime.stringToTime(TIME_DATE_FORMAT)
                                    ).toMinutes()
                                )
                        }
                    }
                }
            }
        }
    }

    private fun setEventDataInItem(roomEvent: RoomEvent, binding: ItemEventSingleRoomBinding, minuteInterval: Long) {
        with(binding) {
            eventCityColourLineUpcoming.setBackgroundColor(roomEvent.colorRoom)
            nameOfBooker.text = roomEvent.userFullName
            roleOfBooker.text = roomEvent.userPosition
            when {
                minuteInterval in 30..59 -> {
                    eventTitleUpcoming.visibility = View.VISIBLE
                    eventTitleUpcoming.text = "${roomEvent.title.substring(0, 30)}..."
                }
                minuteInterval > 60 -> {
                    eventTitleUpcoming.visibility = View.VISIBLE
                    eventTitleUpcoming.text = roomEvent.title
                }
            }
        }
    }

    private fun calculateHeightEventItem(differenceMinutesEvents: Long): Int {
        val partEvent = differenceMinutesEvents.toDouble() / OFFICE_WORKING_TIME_IN_MINUTES
        return (heightSingleRoomGrid * partEvent).toInt()
    }

    companion object {
        private const val EMPTY_EVENT_VIEW_HOLDER_TYPE = 1
        private const val EVENT_VIEW_HOLDER_TYPE = 2
    }

}