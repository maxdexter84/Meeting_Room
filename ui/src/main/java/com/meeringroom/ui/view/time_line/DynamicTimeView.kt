package com.meeringroom.ui.view.time_line

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
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
    private val viewHeight = resources.getDimensionPixelSize(R.dimen.dimens_38_dp)

    var startTime: LocalTime? = null
        set(value) {
            field = value
            if (value != null) {
                with(binding.startTimeTextView) {
                    text = value.timeToString(TIME_FORMAT)
                    val paddingTop = viewHeight * value.minute / MINUTES_IN_HOUR
                    setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)
                }
            } else {
                binding.startTimeTextView.text = ""
            }
        }

    var endTime: LocalTime? = null
        set(value) {
            field = value
            if (value != null) {
                with (binding.endTimeTextView) {
                    text = value.timeToString(TIME_FORMAT)
                    val paddingTop = viewHeight * value.minute / MINUTES_IN_HOUR
                    setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)
                }
            } else {
                binding.endTimeTextView.text = ""
            }
        }

    companion object {
        private const val TIME_FORMAT = "HH:mm"
        private const val MINUTES_IN_HOUR = 60
    }
}
