package com.meetingroom.andersen.feature_landing.presentation.history_of_events_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.meeringroom.ui.view_utils.visibilityIf
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.EventElementHistoryBinding
import com.meetingroom.andersen.feature_landing.domain.entity.HistoryEventData


class HistoryEventAdapter(var onBookersFieldsClick: (View, String) -> Unit) :
    RecyclerView.Adapter<HistoryEventAdapter.HistoryEventViewHolder>() {
    private val events = ArrayList<HistoryEventData>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryEventViewHolder {
        return HistoryEventViewHolder(
            EventElementHistoryBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: HistoryEventViewHolder,
        position: Int
    ) {
        holder.bind(events[position])
    }

    override fun getItemCount() = events.size

    fun setData(newEvents: List<HistoryEventData>) {
        val diffCallback = HistoryEventsDiffCallback(events, newEvents)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        events.clear()
        events.addAll(newEvents)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class HistoryEventViewHolder(private val binding: EventElementHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(historyEventData: HistoryEventData) {
            with(binding) {
                eventTitleUpcoming.text = historyEventData.title
                eventPlannedTimeUpcoming.text =
                    String.format(
                        binding.root.resources.getString(R.string.event_planned_time_upcoming),
                        historyEventData.startTime,
                        historyEventData.endTime
                    )
                eventPlannedDateUpcoming.text = historyEventData.eventDate
                eventRoomUpcoming.text = historyEventData.eventRoom
                eventCityColourLineUpcoming.setBackgroundResource(historyEventData.eventRoomColour)
                eventCardUpcomingRoot.setOnClickListener {
                    flexiblePartOfCardView.visibilityIf(!flexiblePartOfCardView.isVisible)
                    nameOfBooker.text = historyEventData.bookerName
                    roleOfBooker.text = historyEventData.bookerPosition
                    bookerEmail.text = historyEventData.bookerEmail
                    bookerSkype.text = historyEventData.bookerSkype
                    descriptonOfEvent.text = historyEventData.description
                    return@setOnClickListener
                }
                bookerEmail.setOnClickListener {
                    onBookersFieldsClick(it, bookerEmail.text.toString())
                }
                bookerSkype.setOnClickListener {
                    onBookersFieldsClick(it, bookerSkype.text.toString())
                }
            }

        }
    }

    class HistoryEventsDiffCallback(
        private val oldList: List<HistoryEventData>,
        private val newList: List<HistoryEventData>
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