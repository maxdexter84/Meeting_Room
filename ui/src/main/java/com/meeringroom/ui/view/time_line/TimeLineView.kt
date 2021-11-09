package com.meeringroom.ui.view.time_line

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meetingroom.ui.R
import com.meetingroom.ui.databinding.ViewTimeLineBinding
import java.time.LocalTime

@SuppressLint("NewApi")
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
                    if (recyclerView.hasFocus()) {
                        super.onScrolled(recyclerView, dx, dy)
                        onScroll(dy)
                    }
                }
            })
            field = onScroll
        }

    var dynamicStartTime: LocalTime? = null
        set(value) {
            val newItems = timeItems
            when {
                value != null && value.minute == 0 && value.second == 0 -> {
                    val index = newItems.indexOfFirst {  it is TimeItem && it.time == value }
                    (newItems[index] as TimeItem).isSelected = true
                }
                value != null -> {
                    val index = newItems.indexOfFirst {  it is TimeItem && it.time.hour == value.hour }
                    (newItems[index + 1] as EmptyTimeItem).startTime = value
                }
            }
            timeLineAdapter.updateData(newItems)
            field = value
        }

    var dynamicEndTime: LocalTime? = null
        set(value) {
            val newItems = timeItems
            when {
                value != null && value.minute == 0 && value.second == 0 -> {
                    val index = newItems.indexOfFirst { it is TimeItem && it.time == value }
                    (timeLineAdapter.items[index] as TimeItem).isSelected = true
                }
                value != null -> {
                    val index = newItems.indexOfFirst {  it is TimeItem && it.time.hour == value.hour }
                    (newItems[index + 1] as EmptyTimeItem).endTime = value
                }
            }
            timeLineAdapter.updateData(newItems)
            field = value
        }

    private var startHour = DEFAULT_START_HOUR
    private var endHour = DEFAULT_END_HOUR
    private var startHourToShow = DEFAULT_START_HOUR_TO_SHOW
    private var timeLineAdapter: TimeLineAdapter

    init {
        loadAttr(attrs, defStyleAttr)
        getItemsOfTime()
        timeLineAdapter = TimeLineAdapter(timeItems)
        binding.rvTime.apply {
            adapter = timeLineAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        scrollToHour(startHourToShow)
    }

    private fun loadAttr(attrs: AttributeSet?, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.TimeLineView, defStyleAttr, 0) {
            startHour = getInteger(R.styleable.TimeLineView_startHour, DEFAULT_START_HOUR)
            endHour = getInteger(R.styleable.TimeLineView_endHour, DEFAULT_END_HOUR)
            startHourToShow = getInteger(R.styleable.TimeLineView_startHourToShow, DEFAULT_START_HOUR_TO_SHOW)
        }
    }

    @SuppressLint("NewApi")
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

    fun scrollOnDy(dy: Int) {
        binding.rvTime.scrollBy(0, dy)
    }

    private fun scrollToHour(hour: Int) {
        binding.rvTime.scrollToPosition(timeItems.indexOfLast { it is TimeItem && it.time.hour == hour })
    }

    companion object {
        private const val DEFAULT_START_HOUR = 6
        private const val DEFAULT_END_HOUR = 24
        private const val DEFAULT_START_HOUR_TO_SHOW = 8
    }
}

