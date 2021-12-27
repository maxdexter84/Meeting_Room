package com.meetingroom.andersen.feature_landing.presentation.upcoming_events_fragment

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
        private lateinit var date: String

        fun bind(upcomingEventData: UpcomingEventData) {
            with(binding) {
                upcomingEventData.startTime = getTime(upcomingEventData.startDateTime)
                upcomingEventData.endTime = getTime(upcomingEventData.endDateTime)
                upcomingEventData.eventDate = date
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
                upcomingEventData.eventRoomColour = createColor(upcomingEventData.room)
                eventCityColourLineUpcoming.setBackgroundResource(upcomingEventData.eventRoomColour)
                eventCardUpcomingRoot.setOnClickListener { onEventClicked(upcomingEventData) }
                if (!upcomingEventData.reminderActive) {
                    eventReminderBellUpcoming.setImageResource(R.drawable.ic_disable_bell)
                    eventReminderCounterUpcoming.visibilityIf(false)
                } else {
                    eventReminderBellUpcoming.setImageResource(R.drawable.ic_enable_bell)
                    eventReminderCounterUpcoming.visibilityIf(true)
                    eventReminderCounterUpcoming.text = upcomingEventData.reminderRemainingTime
                }
            }
        }

        private fun createColor(nameRoom: String): Int {
            val listColor = listOf(
                R.color.purple_light,
                R.color.purple_dark,
                R.color.purple,
                R.color.teal_dark,
                R.color.teal_light
            )

            return when (nameRoom) {
                "Green" -> R.color.green
                "Yellow" -> R.color.yellow_for_selected_item
                "Gray" -> R.color.gray_60_dark
                "Red" -> R.color.dark_red
                "Blue" -> R.color.blue
                "Pink" -> R.color.pink
                "Purple" -> R.color.purple_light
                "Orange" -> R.color.orange
                else -> listColor.random()
            }
        }

        private fun getTime(stringDateAndTime: String): String {
            val strings = stringDateAndTime.split("T")
            date = strings[0]
            return strings[1].dropLast(3)
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


