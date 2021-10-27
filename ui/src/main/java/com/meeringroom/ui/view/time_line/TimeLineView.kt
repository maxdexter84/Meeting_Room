package com.meeringroom.ui.view.time_line

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    var startTime: LocalTime = LocalTime.of(6, 0, 0)
    var endTime: LocalTime = LocalTime.of(23, 0, 0)

    init {
        binding.rvTime.apply {
            val items = listOf("09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00")
            adapter = TimeLineAdapter(items)
            //adapter = TimeLineAdapter(getItemsOfTime())
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    /*@SuppressLint("NewApi")
    fun getItemsOfTime(): List<String> {
        var time = startTime
        val items = mutableListOf<String>()
        while (!time.isAfter(endTime)) {
            items.add(time.timeToString(TIME_FORMAT))
            time = time.plusHours(1)
        }
        return items
    }*/

    fun scrollOnDy(dy: Int) {
        binding.rvTime.scrollBy(0, dy)
    }
    
    fun scrollOnPosition(position: Int) {
        binding.rvTime.scrollToPosition(position)
    }
    
    companion object {
        private const val TIME_FORMAT = "HH:00"
    }
}