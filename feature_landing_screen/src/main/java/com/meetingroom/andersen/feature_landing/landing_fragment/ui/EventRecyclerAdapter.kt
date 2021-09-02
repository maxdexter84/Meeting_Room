package com.meetingroom.andersen.feature_landing.landing_fragment.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.EventElementUpcomingBinding
import com.meetingroom.andersen.feature_landing.landing_fragment.model.EventUpcomingData

class EventRecyclerAdapter :
    ListAdapter<EventUpcomingData, EventRecyclerAdapter.UpcomingEventViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingEventViewHolder {
        val binding =
            EventElementUpcomingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UpcomingEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpcomingEventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class UpcomingEventViewHolder(private val binding: EventElementUpcomingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(eventUpcomingData: EventUpcomingData) {
            with(binding) {
                eventTitleUpcoming.text = eventUpcomingData.title
                eventPlannedTimeUpcoming.text =
                    "${eventUpcomingData.startTime}-${eventUpcomingData.endTime}"
                eventPlannedDateUpcoming.text = eventUpcomingData.eventDate
                eventRoomUpcoming.text = eventUpcomingData.eventRoom
                eventCityColourLineUpcoming.setBackgroundColor(eventUpcomingData.eventRoomColour)
                if (!eventUpcomingData.reminderActive) {
                    eventReminderBellUpcoming.setImageResource(R.drawable.ic_disable_bell)
                    eventReminderCounterUpcoming.visibility = View.INVISIBLE
                } else {
                    eventReminderBellUpcoming.setImageResource(R.drawable.ic_enable_bell)
                    eventReminderCounterUpcoming.text = eventUpcomingData.reminderRemainingTime
                }
                eventReminderBellUpcoming.setOnClickListener {
                    eventUpcomingData.reminderActive = !eventUpcomingData.reminderActive
                }
            }
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<EventUpcomingData>() {
        override fun areItemsTheSame(oldItem: EventUpcomingData, newItem: EventUpcomingData) =
            oldItem.title == newItem.title && oldItem.reminderActive == newItem.reminderActive

        override fun areContentsTheSame(oldItem: EventUpcomingData, newItem: EventUpcomingData) =
            oldItem == newItem
    }
}