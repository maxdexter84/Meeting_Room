package com.meetingroom.andersen.feature_landing.presentation.upcoming_events_fragment

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.core_module.utils.dateToString
import com.example.core_module.utils.stringToDate
import com.meeringroom.ui.view_utils.visibilityIf
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.EventElementUpcomingBinding
import com.meetingroom.andersen.feature_landing.domain.entity.UpcomingEventData
import com.meetingroom.andersen.feature_landing.presentation.modify_upcoming_fragment.setReminderTime

class UpcomingEventAdapter(var onEventClicked: (UpcomingEventData) -> Unit) :
    RecyclerView.Adapter<UpcomingEventAdapter.UpcomingEventViewHolder>() {

    private val events = ArrayList<UpcomingEventData>()
    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpcomingEventViewHolder {
        context = parent.context
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
                eventPlannedDateUpcoming.text = upcomingEventData.eventDate.stringToDate(INPUT_DATE_FORMAT)
                        .dateToString(OUTPUT_DATE_FORMAT)
                eventRoomUpcoming.text = upcomingEventData.room
                eventCityColourLineUpcoming.setBackgroundColor(Color.parseColor(upcomingEventData.color))
                eventCardUpcomingRoot.setOnClickListener { onEventClicked(upcomingEventData) }
                if (!upcomingEventData.reminderActive) {
                    eventReminderBellUpcoming.setImageResource(R.drawable.ic_disable_bell)
                    eventReminderCounterUpcoming.visibilityIf(false)
                } else {
                    eventReminderBellUpcoming.setImageResource(R.drawable.ic_enable_bell)
                    eventReminderCounterUpcoming.visibilityIf(true)
                    eventReminderCounterUpcoming.text = setReminderTime(context, upcomingEventData.reminderRemainingTime ?: "0")
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

    companion object {
        private const val INPUT_DATE_FORMAT = "yyyy-MM-d"
        private const val OUTPUT_DATE_FORMAT = "d MMM YYYY"
    }
}


