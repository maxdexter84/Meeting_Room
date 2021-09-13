package com.meetingroom.andersen.feature_landing.upcoming_events_fragment.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.meeringroom.ui.view_utils.visibilityIf
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.EventElementUpcomingBinding
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.model.UpcomingEventData

class UpcomingEventAdapter(var onEventClicked: (UpcomingEventData) -> Unit) :
    RecyclerView.Adapter<UpcomingEventAdapter.UpcomingEventViewHolder>() {

    private val events = ArrayList<UpcomingEventData>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpcomingEventViewHolder {
        return UpcomingEventViewHolder(
            EventElementUpcomingBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: UpcomingEventViewHolder,
        position: Int
    ) {
        holder.bind(events[position])
    }

    override fun getItemCount() = events.size

    fun setData(newEvents: List<UpcomingEventData>) {
        val diffCallback = UpcomingEventsDiffCallback(events, newEvents)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        events.clear()
        events.addAll(newEvents)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class UpcomingEventViewHolder(private val binding: EventElementUpcomingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(upcomingEventData: UpcomingEventData) {
            with(binding) {
                eventTitleUpcoming.text = upcomingEventData.title
                eventPlannedTimeUpcoming.text =
                    String.format(
                        binding.root.resources.getString(R.string.event_planned_time_upcoming),
                        upcomingEventData.startTime,
                        upcomingEventData.endTime
                    )
                eventPlannedDateUpcoming.text = upcomingEventData.eventDate
                eventRoomUpcoming.text = upcomingEventData.eventRoom
                eventCityColourLineUpcoming.setBackgroundResource(upcomingEventData.eventRoomColour)
                if (!upcomingEventData.reminderActive) {
                    eventReminderBellUpcoming.setImageResource(R.drawable.ic_disable_bell)
                    eventReminderCounterUpcoming.visibilityIf(false)
                } else {
                    eventReminderBellUpcoming.setImageResource(R.drawable.ic_enable_bell)
                    eventReminderCounterUpcoming.visibilityIf(true)
                    eventReminderCounterUpcoming.text = upcomingEventData.reminderRemainingTime
                    eventReminderBellUpcoming.setOnClickListener {
                        onEventClicked(upcomingEventData)
                    }
                }
            }
        }
    }

    class UpcomingEventsDiffCallback(
        private val oldList: List<UpcomingEventData>,
        private val newList: List<UpcomingEventData>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].title == newList[newItemPosition].title
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}


