package com.meetingroom.andersen.feature_landing.presentation.history_of_events_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.core_module.utils.dateToString
import com.example.core_module.utils.stringToDate
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
        private lateinit var date: String

        fun bind(historyEventData: HistoryEventData) {
            with(binding) {
                with(historyEventData){
                    startTime = getTime(historyEventData.startDateTime)
                    endTime = getTime(historyEventData.endDateTime)
                    eventDate = date
                }
                eventTitleUpcoming.text = historyEventData.title
                eventPlannedTimeUpcoming.text =
                    String.format(
                        binding.root.resources.getString(R.string.event_planned_time_upcoming),
                        historyEventData.startTime,
                        historyEventData.endTime
                    )
                eventPlannedDateUpcoming.text = historyEventData.eventDate.stringToDate(INPUT_DATE_FORMAT)
                    .dateToString(OUTPUT_DATE_FORMAT)
                eventRoomUpcoming.text = historyEventData.room
                historyEventData.eventRoomColour = createColor(historyEventData.room)
                eventCityColourLineUpcoming.setBackgroundResource(historyEventData.eventRoomColour)
                eventCardUpcomingRoot.setOnClickListener {
                    flexiblePartOfCardView.visibilityIf(!flexiblePartOfCardView.isVisible)
                    nameOfBooker.text = historyEventData.userFullName
                    roleOfBooker.text = historyEventData.userPosition
                    bookerEmail.text = historyEventData.userEmail
                    bookerSkype.text = historyEventData.userSkype
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

    companion object {
        private const val INPUT_DATE_FORMAT = "yyyy-MM-d"
        private const val OUTPUT_DATE_FORMAT = "d MMM YYYY"
    }
}