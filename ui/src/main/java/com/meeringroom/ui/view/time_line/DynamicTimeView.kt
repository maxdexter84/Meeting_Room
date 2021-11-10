package com.meeringroom.ui.view.time_line

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.core_module.utils.timeToString
import com.meetingroom.ui.databinding.ViewDynamicTimeItemBinding
import java.time.LocalTime


@SuppressLint("NewApi")
class DynamicTimeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    val binding = ViewDynamicTimeItemBinding.inflate(LayoutInflater.from(context), this, true)

    var startTime: LocalTime? = null
        set(value) {
            field = value
            if (value != null) {
                binding.startTimeTextView.text = value.timeToString(TIME_FORMAT)
                val params = LayoutParams(binding.startTimeTextView.layoutParams)
                params.topMargin = height * MINUTES_IN_HOUR / value.minute
                Log.d("TIMELINE", " set Margin to start time ${params.topMargin}")
                binding.startTimeTextView.layoutParams = params
                binding.startTimeTextView.requestLayout()
            } else {
                binding.startTimeTextView.text = ""
            }
        }

    var endTime: LocalTime? = null
        set(value) {
            field = value
            if (value != null) {

                binding.endTimeTextView.text = value.timeToString(TIME_FORMAT)
                val params = LayoutParams(binding.endTimeTextView.layoutParams)
                params.topMargin = height * MINUTES_IN_HOUR / value.minute
                Log.d("TIMELINE", " set Margin to end time ${params.topMargin}")
                binding.endTimeTextView.layoutParams = params
                binding.endTimeTextView.requestLayout()
            } else {
                binding.endTimeTextView.text = ""
            }
        }

    companion object {
        private const val TIME_FORMAT = "HH:mm"
        private const val MINUTES_IN_HOUR = 60
    }
}
