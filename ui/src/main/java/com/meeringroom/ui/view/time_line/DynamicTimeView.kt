package com.meeringroom.ui.view.time_line

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.core_module.utils.timeToString
import com.meetingroom.ui.R
import com.meetingroom.ui.databinding.ViewDynamicTimeItemBinding
import java.time.LocalTime


@SuppressLint("NewApi")
class DynamicTimeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val binding = ViewDynamicTimeItemBinding.inflate(LayoutInflater.from(context), this, true)
    private val viewHeight = resources.getDimensionPixelSize(R.dimen.dimens_34_dp)
    private val minSpace = resources.getDimensionPixelSize(R.dimen.dimens_4_dp)
    private var startTimePadding = 0
    private var endTimePadding = 0

    var startTime: LocalTime? = null
        set(value) {
            field = value
            if (value != null) {
                binding.startTimeTextView.text = value.timeToString(TIME_FORMAT)
                when {
                    value.minute < MIN_MINUTE_WITH_PADDING -> startTimePadding = 0
                    value.minute in MIN_MINUTE_WITH_PADDING..MAX_MINUTE_WITH_PADDING ->
                        startTimePadding = viewHeight * value.minute / MINUTES_IN_HOUR
                    value.minute > MAX_MINUTE_WITH_PADDING -> startTimePadding = viewHeight
                }
                checkSpace()
                setPadding(startTimePadding, binding.startTimeTextView)
                setPadding(endTimePadding, binding.endTimeTextView)
            } else {
                binding.startTimeTextView.text = ""
            }
        }

    var endTime: LocalTime? = null
        set(value) {
            field = value
            if (value != null) {
                binding.endTimeTextView.text = value.timeToString(TIME_FORMAT)
                when {
                    value.minute < MIN_MINUTE_WITH_PADDING -> endTimePadding = 0
                    value.minute in MIN_MINUTE_WITH_PADDING..MAX_MINUTE_WITH_PADDING ->
                        endTimePadding = viewHeight * value.minute / MINUTES_IN_HOUR
                    value.minute > MAX_MINUTE_WITH_PADDING -> endTimePadding = viewHeight
                }
                checkSpace()
                setPadding(startTimePadding, binding.startTimeTextView)
                setPadding(endTimePadding, binding.endTimeTextView)
            } else {
                binding.endTimeTextView.text = ""
            }
        }

    private fun setPadding(paddingTop: Int, view: View) {
        with(view) {
            setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)
        }
    }

    private fun checkSpace() {
        if (kotlin.math.abs(startTimePadding - endTimePadding) > minSpace) {
            when {
                startTimePadding < minSpace -> endTimePadding += minSpace
                endTimePadding > viewHeight - minSpace -> startTimePadding -= minSpace
                else -> {
                    endTimePadding += minSpace / 2
                    startTimePadding -= minSpace / 2
                }
            }
        }
    }

    companion object {
        private const val TIME_FORMAT = "HH:mm"
        private const val MINUTES_IN_HOUR = 60
        private const val MIN_MINUTE_WITH_PADDING = 15
        private const val MAX_MINUTE_WITH_PADDING = 45
    }
}
