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

    val binding = ViewTimeLineBinding.inflate(LayoutInflater.from(context), this, true)
    private lateinit var timeItems: MutableList<String?>
    private val textHeight = resources.getDimensionPixelSize(R.dimen.dimens_16_dp)

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

    private var startHour = DEFAULT_START_HOUR
    private var endHour = DEFAULT_END_HOUR
    private var startHourToShow = DEFAULT_START_HOUR_TO_SHOW
    private var hourHeight = 0
    private var topMargin = 0

    init {
        loadAttr(attrs, defStyleAttr)
        getItemsOfTime()
        binding.rvTime.apply {
            adapter = TimeLineAdapter(timeItems, hourHeight, textHeight)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

    }

    private fun loadAttr(attrs: AttributeSet?, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.TimeLineView, defStyleAttr, 0) {
            startHour = getInteger(R.styleable.TimeLineView_startHour, DEFAULT_START_HOUR)
            endHour = getInteger(R.styleable.TimeLineView_endHour, DEFAULT_END_HOUR)
            startHourToShow = getInteger(R.styleable.TimeLineView_startHourToShow, DEFAULT_START_HOUR_TO_SHOW)
            hourHeight = getDimensionPixelSize(R.styleable.TimeLineView_hourHeight, 0)
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
            timeItems.add(time.timeToString(TIME_FORMAT))
            timeItems.add(null)
            time = time.plusHours(1)
            if (time.hour == 0) return
        }
    }

    fun scrollOnDy(dy: Int) {
        if (binding.rvTime.scrollState == RecyclerView.SCROLL_STATE_IDLE)
        binding.rvTime.scrollBy(0, dy)
    }

    private fun scrollToHour(hour: Int) {
        binding.rvTime.scrollToPosition(timeItems.indexOfLast { it?.stringToTime(TIME_FORMAT)?.hour == hour })
    }

    companion object {
        private const val TIME_FORMAT = "HH:00"
        private const val DEFAULT_START_HOUR = 6
        private const val DEFAULT_END_HOUR = 24
        private const val DEFAULT_START_HOUR_TO_SHOW = 8
    }
}

