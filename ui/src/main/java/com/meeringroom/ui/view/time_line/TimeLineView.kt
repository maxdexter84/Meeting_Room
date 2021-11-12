package com.meeringroom.ui.view.time_line

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meetingroom.ui.R
import com.meetingroom.ui.databinding.ViewTimeLineBinding
import java.time.LocalTime


class TimeLineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val binding = ViewTimeLineBinding.inflate(LayoutInflater.from(context), this, true)
    private lateinit var timeItems: MutableList<TimeLineItem>

    var onScroll: (dy: Int) -> Unit = {}
        set(onScroll) {
            binding.rvTime.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (recyclerView.scrollState != RecyclerView.SCROLL_STATE_IDLE) {
                        super.onScrolled(recyclerView, dx, dy)
                        onScroll(dy)
                    }
                }
            })
            field = onScroll
        }

    var dynamicStartTime: LocalTime? = null
        set(value) {
            field?.let {
                when (val oldItem = getItem(it)) {
                    is TimeItem -> oldItem.isSelected = false
                    is EmptyTimeItem -> oldItem.startTime = null
                }
                timeLineAdapter.notifyItemChanged(getItemPosition(field!!))
            }
            value?.let {
                when (val newItem = getItem(value)) {
                    is TimeItem -> newItem.isSelected = true
                    is EmptyTimeItem -> newItem.startTime = value
                }
                timeLineAdapter.notifyItemChanged(getItemPosition(value))
            }
            field = value
        }

    var dynamicEndTime: LocalTime? = null
        set(value) {
            field?.let {
                when (val oldItem = getItem(it)) {
                    is TimeItem -> oldItem.isSelected = false
                    is EmptyTimeItem -> oldItem.endTime = null
                }
                timeLineAdapter.notifyItemChanged(getItemPosition(field!!))
            }
            value?.let {
                when (val newItem = getItem(value)) {
                    is TimeItem -> newItem.isSelected = true
                    is EmptyTimeItem -> newItem.endTime = value
                }
                timeLineAdapter.notifyItemChanged(getItemPosition(value))
            }
            field = value
        }

    private var startHour = DEFAULT_START_HOUR
    private var endHour = DEFAULT_END_HOUR
    private var startHourToShow = DEFAULT_START_HOUR_TO_SHOW
    private var timeLineAdapter: TimeLineAdapter

    init {
        loadAttr(attrs, defStyleAttr)
        getItemsOfTime()
        timeLineAdapter = TimeLineAdapter(timeItems,
            ContextCompat.getColor(context, R.color.yellow),
            ContextCompat.getColor(context, R.color.text_unvisible))
        binding.rvTime.apply {
            adapter = timeLineAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun loadAttr(attrs: AttributeSet?, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.TimeLineView, defStyleAttr, 0) {
            startHour = getInteger(R.styleable.TimeLineView_startHour, DEFAULT_START_HOUR)
            endHour = getInteger(R.styleable.TimeLineView_endHour, DEFAULT_END_HOUR)
            startHourToShow = getInteger(R.styleable.TimeLineView_startHourToShow, DEFAULT_START_HOUR_TO_SHOW)
        }
    }

    private fun getItemsOfTime() {
        if (startHour >= endHour) {
            startHour = DEFAULT_START_HOUR
            endHour = DEFAULT_END_HOUR
            startHourToShow = DEFAULT_START_HOUR_TO_SHOW
        }
        var time = LocalTime.of(startHour, 0 , 0)
        timeItems = mutableListOf()
        while (time.hour < endHour) {
            timeItems.add(TimeItem(time, false))
            timeItems.add(EmptyTimeItem(null, null))
            time = time.plusHours(1)
            if (time.hour == 0) {
                timeItems.add(TimeItem(time, false))
                return
            }
        }
    }

    private fun getItemPosition(time: LocalTime): Int {
        val index = timeItems.indexOfFirst{  it is TimeItem && it.time.hour == time.hour }
        return if (time.minute == 0) { index } else { index + 1 }
    }

    private fun getItem(time: LocalTime): TimeLineItem {
        return timeItems[getItemPosition(time)]
    }

    private fun scrollToHour(hour: Int) {
        binding.rvTime.scrollToPosition(timeItems.indexOfLast { it is TimeItem && it.time.hour == hour })
    }

    fun scrollOnDy(dy: Int) {
        if (binding.rvTime.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
            binding.rvTime.scrollBy(0, dy)
        }
    }

    companion object {
        private const val DEFAULT_START_HOUR = 6
        private const val DEFAULT_END_HOUR = 24
        private const val DEFAULT_START_HOUR_TO_SHOW = 8
    }
}
