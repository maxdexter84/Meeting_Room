package com.meetingroom.andersen.feature_landing.upcoming_events_fragment.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.EventElementUpcomingBinding
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.model.UpcomingEventData

class UpcomingEventAdapter :
    ListAdapter<UpcomingEventData, UpcomingEventAdapter.UpcomingEventViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingEventViewHolder {
        val binding =
            EventElementUpcomingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UpcomingEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpcomingEventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UpcomingEventViewHolder(private val binding: EventElementUpcomingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(upcomingEventData: UpcomingEventData) {
            with(binding) {
                eventTitleUpcoming.text = upcomingEventData.title
                eventPlannedTimeUpcoming.text =
                    "${upcomingEventData.startTime}-${upcomingEventData.endTime}"
                eventPlannedDateUpcoming.text = upcomingEventData.eventDate
                eventRoomUpcoming.text = upcomingEventData.eventRoom
                eventCityColourLineUpcoming.setBackgroundResource(upcomingEventData.eventRoomColour)
                if (!upcomingEventData.reminderActive) {
                    eventReminderBellUpcoming.setImageResource(R.drawable.ic_disable_bell)
                    eventReminderCounterUpcoming.visibility = View.INVISIBLE
                } else {
                    eventReminderBellUpcoming.setImageResource(R.drawable.ic_enable_bell)
                    eventReminderCounterUpcoming.text = upcomingEventData.reminderRemainingTime
                }
            }
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<UpcomingEventData>() {
        override fun areItemsTheSame(oldItem: UpcomingEventData, newItem: UpcomingEventData) =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: UpcomingEventData, newItem: UpcomingEventData) =
            oldItem == newItem
    }
}