package com.meeringroom.ui.view.time_line

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.core_module.utils.timeToString
import com.meetingroom.ui.R
import com.meetingroom.ui.databinding.ViewDynamicTimeItemBinding
import java.time.LocalTime


class DynamicTimeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val binding = ViewDynamicTimeItemBinding.inflate(LayoutInflater.from(context), this, true)
    private val timeHeight = resources.getDimensionPixelSize(R.dimen.dimens_16_dp)
    var viewHeight = resources.getDimensionPixelSize(R.dimen.dimens_66_dp)
        set(value) {
            field = value - 2 * timeHeight
            layoutParams.height = value - timeHeight
        }

    private val minSpace = resources.getDimensionPixelSize(R.dimen.dimens_4_dp)
    private var startTimeMargin = 0
    private var endTimeMargin = 0

    var startTime: LocalTime? = null
        set(value) {
            field = value
            if (value != null) {
                binding.startTimeTextView.text = value.timeToString(TIME_FORMAT)
                startTimeMargin = getMarginByMinute(value.minute)
                setMargins()
            } else {
                binding.startTimeTextView.text = ""
            }
        }

    var endTime: LocalTime? = null
        set(value) {
            field = value
            if (value != null) {
                binding.endTimeTextView.text = value.timeToString(TIME_FORMAT)
                endTimeMargin = getMarginByMinute(value.minute)
                setMargins()
            } else {
                binding.endTimeTextView.text = ""
            }
        }

    private fun setMargins() {
        checkSpace()
        with(binding.startTimeTextView) {
            (layoutParams as MarginLayoutParams).topMargin = startTimeMargin
        }
        with(binding.endTimeTextView) {
            (layoutParams as MarginLayoutParams).topMargin = endTimeMargin
        }
    }

    private fun getMarginByMinute(minute: Int): Int {
        return when (minute) {
            in 0..MIN_MINUTE_WITH_MARGIN -> 0
            in MIN_MINUTE_WITH_MARGIN..MAX_MINUTE_WITH_MARGIN -> viewHeight * minute / MINUTES_IN_HOUR
            else -> viewHeight - minSpace
        }
    }

    private fun checkSpace() {
        if (kotlin.math.abs(startTimeMargin - endTimeMargin) > minSpace) {
            when {
                startTimeMargin < minSpace -> endTimeMargin += minSpace
                endTimeMargin > viewHeight - minSpace -> startTimeMargin -= minSpace
                else -> {
                    endTimeMargin += minSpace / 2
                    startTimeMargin -= minSpace / 2
                }
            }
        }
    }

    companion object {
        private const val TIME_FORMAT = "HH:mm"
        private const val MINUTES_IN_HOUR = 60
        private const val MIN_MINUTE_WITH_MARGIN = 15
        private const val MAX_MINUTE_WITH_MARGIN = 45
    }
}
