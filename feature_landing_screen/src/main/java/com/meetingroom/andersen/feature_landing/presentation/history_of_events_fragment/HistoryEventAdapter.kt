package com.meetingroom.andersen.feature_landing.presentation.history_of_events_fragment

import android.graphics.Color
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core_module.utils.dateToString
import com.example.core_module.utils.stringToDate
import com.meeringroom.ui.view_utils.visibilityIf
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.EventElementHistoryBinding
import com.meetingroom.andersen.feature_landing.domain.entity.HistoryEventData


class HistoryEventAdapter(var layoutMgr: LinearLayoutManager,
    private val onBookersFieldsClick: (View, String) -> Unit,
    private val onBookersSkypeClick: (String) -> Unit
) :
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
                eventPlannedDateUpcoming.text = historyEventData.eventDate.stringToDate(INPUT_DATE_FORMAT)
                    .dateToString(OUTPUT_DATE_FORMAT)
                eventRoomUpcoming.text = historyEventData.room
                eventCityColourLineUpcoming.setBackgroundColor(Color.parseColor(historyEventData.color))
                nameOfBooker.text = historyEventData.userFullName
                roleOfBooker.text = historyEventData.userPosition
                bookerEmail.text = historyEventData.userEmail
                bookerSkype.text = historyEventData.userSkype
                descriptonOfEvent.text = historyEventData.description

                if(historyEventData.isOpened){
                    flexiblePartOfCardView.visibility = View.VISIBLE
                } else {
                    flexiblePartOfCardView.visibility = View.GONE
                }

                eventCardUpcomingRoot.setOnClickListener {
                    historyEventData.isOpened = !historyEventData.isOpened
                    flexiblePartOfCardView.visibilityIf(!flexiblePartOfCardView.isVisible)
                    scrollToFitExpandedCardView(it)
                    return@setOnClickListener
                }
                bookerEmail.setOnClickListener {
                    onBookersFieldsClick(it, bookerEmail.text.toString())
                }
                bookerSkype.setOnClickListener {
                    onBookersSkypeClick(bookerSkype.text.toString())
                }
            }
        }

        private fun scrollToFitExpandedCardView(view: View) {
            view.measure(
                View.MeasureSpec.makeMeasureSpec(
                    view.width, View.MeasureSpec.EXACTLY
                ),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            with(binding) {
                if (flexiblePartOfCardView.isVisible) {
                    val bottomView =
                        layoutMgr.findViewByPosition(layoutMgr.findLastVisibleItemPosition())
                    if (bottomView != null) {
                        val r = Rect()
                        bottomView.getGlobalVisibleRect(r)
                        val marginHeight = view.resources.getDimensionPixelSize(R.dimen.dimens_12_dp)
                        if (adapterPosition == layoutMgr.findLastVisibleItemPosition()) {
                            layoutMgr.offsetChildrenVertical(r.height() - flexiblePartOfCardView.measuredHeight - bottomView.height - marginHeight)
                        }
                        if (adapterPosition == layoutMgr.findLastVisibleItemPosition() - 1) {
                            layoutMgr.offsetChildrenVertical(r.height() - flexiblePartOfCardView.measuredHeight)
                        }
                    }
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

    companion object {
        private const val INPUT_DATE_FORMAT = "yyyy-MM-d"
        private const val OUTPUT_DATE_FORMAT = "d MMM YYYY"
    }
}