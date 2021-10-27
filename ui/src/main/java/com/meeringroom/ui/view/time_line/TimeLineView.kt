package com.meeringroom.ui.view.time_line

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core_module.utils.stringToTime
import com.example.core_module.utils.timeToString
import com.meetingroom.ui.R
import com.meetingroom.ui.databinding.ViewTimeLineBinding
import java.time.LocalTime

@SuppressLint("NewApi")
class TimeLineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewTimeLineBinding.inflate(LayoutInflater.from(context), this, true)
    private lateinit var timeItems: MutableList<String?>

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

    private var startTime: LocalTime = LocalTime.of(DEFAULT_START_TIME, 0, 0)
    private var endTime: LocalTime = LocalTime.of(DEFAULT_END_TIME, 0, 0)
    private var startTimeToShow: LocalTime = LocalTime.of(DEFAULT_START_TIME_TO_SHOW, 0, 0)

    init {
        loadAttr(attrs, defStyleAttr)
        getItemsOfTime()
        binding.rvTime.apply {
            adapter = TimeLineAdapter(timeItems)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        scrollToTime(startTimeToShow)
    }

    private fun loadAttr(attrs: AttributeSet?, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.TimeLineView, defStyleAttr, 0) {
            if (this.hasValue(R.styleable.TimeLineView_startHour)) startTime = LocalTime.of(getInteger(R.styleable.TimeLineView_startHour, DEFAULT_START_TIME), 0, 0)
            if (this.hasValue(R.styleable.TimeLineView_endHour)) endTime = LocalTime.of(getInteger(R.styleable.TimeLineView_endHour, DEFAULT_END_TIME), 0, 0)
            if (this.hasValue(R.styleable.TimeLineView_startHourToShow)) startTimeToShow = LocalTime.of(getInteger(R.styleable.TimeLineView_startHourToShow, DEFAULT_START_TIME_TO_SHOW), 0, 0)
        }
    }

    @SuppressLint("NewApi")
    fun getItemsOfTime() {
        var time = startTime
        timeItems = mutableListOf()
        while (!time.isAfter(endTime)) {
            timeItems.add(time.timeToString(TIME_FORMAT))
            time = time.plusHours(1)
            timeItems.addAll(listOf(null, null, null, null, null))
        }
    }

    fun scrollOnDy(dy: Int) {
        binding.rvTime.scrollBy(0, dy)
    }
    
    fun scrollToTime(time: LocalTime) {
        binding.rvTime.scrollToPosition(timeItems.indexOfLast { it?.stringToTime(TIME_FORMAT)?.hour == time.hour })
    }
    
    companion object {
        private const val TIME_FORMAT = "HH:00"
        private const val DEFAULT_START_TIME = 6
        private const val DEFAULT_END_TIME = 18
        private const val DEFAULT_START_TIME_TO_SHOW = 8
    }
}