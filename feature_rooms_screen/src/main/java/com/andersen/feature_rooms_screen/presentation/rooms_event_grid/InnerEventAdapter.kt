package com.andersen.feature_rooms_screen.presentation.rooms_event_grid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.andersen.feature_rooms_screen.domain.entity.RoomEvent
import com.example.core_module.utils.stringToTime
import com.meeringroom.ui.view.event_view.EventRectangleView
import com.meetingroom.andersen.feature_rooms_screen.databinding.ItemEventBinding

class InnerEventAdapter(val isStartMainListPosition: Boolean, val eventList: List<RoomEvent>) :
    RecyclerView.Adapter<InnerEventAdapter.EventRoomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventRoomViewHolder =
        EventRoomViewHolder(ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: EventRoomViewHolder, position: Int) {
        if (eventList.isNotEmpty()) holder.bind(eventList, position, isStartMainListPosition)
    }

    override fun getItemCount(): Int {
        return ITEM_COUNT
    }

    class EventRoomViewHolder(
        private val binding: ItemEventBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(eventList: List<RoomEvent>, position: Int, isStartMainListPosition: Boolean) {
            with(binding.eventView) {
                startBordersIsVisible = isStartMainListPosition.not()
                eventList.map {

                    val startTime = it.startDateTime.stringToTime(TIME_FORMAT)
                    val endTime = it.endDateTime.stringToTime(TIME_FORMAT)
                    val startTimeRange =
                        if (position > 3) "${position + START_WORK_HOUR}:00".stringToTime(TIME_FORMAT) else "0${position + START_WORK_HOUR}:00".stringToTime(
                            TIME_FORMAT
                        )
                    val endTimeRange =
                        if (position > 2) "${position + START_WORK_HOUR + 1}:00".stringToTime(TIME_FORMAT) else "0${position + START_WORK_HOUR + 1}:00".stringToTime(
                            TIME_FORMAT
                        )

                    var startMinutes = NULL_MINUTE_IN_EVENT
                    var endMinutes = NULL_MINUTE_IN_EVENT
                    val compareStartTime = startTime.compareTo(startTimeRange)
                    val compareEndTime = endTime.compareTo(endTimeRange)


                    when {
                        compareStartTime == 0 -> {
                            startMinutes = NULL_MINUTE_IN_EVENT
                            activeEventSetOnClickListener(this, it)
                        }
                        compareStartTime == 1 && startTime.isBefore(endTimeRange) -> startMinutes = startTime.minute
                        compareStartTime == -1 && endTime.isAfter(startTimeRange) -> {
                            startMinutes = NULL_MINUTE_IN_EVENT
                            topBordersIsVisible = false
                            activeEventSetOnClickListener(this, it)
                        }
                    }

                    when {
                        compareEndTime == 0 -> {
                            endMinutes = LAST_MINUTE_IN_EVENT
                            activeEventSetOnClickListener(this, it)
                        }
                        compareEndTime == -1 && endTime.isAfter(startTimeRange) -> endMinutes = endTime.minute
                        compareEndTime == 1 && startTime.isBefore(endTimeRange) -> {
                            endMinutes = LAST_MINUTE_IN_EVENT
                            bottomBordersIsVisible = false
                            activeEventSetOnClickListener(this, it)
                        }
                    }

                    if (startMinutes > NULL_MINUTE_IN_EVENT || endMinutes > NULL_MINUTE_IN_EVENT) {
                        setMinuteInterval(startMinutes, endMinutes)
                        setColorEvent(it.colorRoom)
                    }
                    isUserOwnEvent = it.isUserOwnEvent
                }
            }
        }

        private fun activeEventSetOnClickListener(eventRectangleView: EventRectangleView, roomEvent: RoomEvent) {
            eventRectangleView.setOnClickListener {
                eventRectangleView.findNavController().navigate(RoomsEventGridFragmentDirections.actionRoomsFragmentToEventDetailDialog(roomEvent))
            }
        }
    }

    companion object {
        const val ITEM_COUNT = 18
        const val LAST_MINUTE_IN_EVENT = 60
        const val NULL_MINUTE_IN_EVENT = 0
        const val START_WORK_HOUR = 6
        private const val TIME_FORMAT = "HH:mm"
    }
}