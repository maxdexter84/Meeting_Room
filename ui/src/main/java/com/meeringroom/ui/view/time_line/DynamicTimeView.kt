package com.meeringroom.ui.view.time_line

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
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

    private val textHeight = resources.getDimensionPixelSize(R.dimen.dimens_16_dp)
    private val viewHeight = resources.getDimensionPixelSize(R.dimen.dimens_66_dp)

    init {
        layoutParams = LayoutParams(0, viewHeight - textHeight)
    }

    var startTime: LocalTime? = null
        set(value) {
            field = value
            if (value != null) {
                binding.startTimeTextView.text = value.timeToString(TIME_FORMAT)
            }
        }

    var endTime: LocalTime? = null
        set(value) {
            field = value
            if (value != null) {
                binding.endTimeTextView.text = value.timeToString(TIME_FORMAT)
            }
        }

    companion object {
        private const val TIME_FORMAT = "HH:mm"
    }
}
