package com.meeringroom.ui.view.time_line

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.core_module.utils.timeToString
import com.meetingroom.ui.R


class TimeLineAdapter(var items: List<TimeLineItem>, var dynamicTimeColor: Int, val timeColor: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EMPTY_VIEW_HOLDER_TYPE -> {
                val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.empty_time_line_item, parent, false)
                EmptyViewHolder(adapterLayout)
            }
            else -> {
                val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.time_line_item, parent, false)
                TimeViewHolder(adapterLayout)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            EMPTY_VIEW_HOLDER_TYPE -> (holder as EmptyViewHolder).bind(position)
            TIME_VIEW_HOLDER_TYPE -> (holder as TimeViewHolder).bind(position)
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is EmptyTimeItem -> EMPTY_VIEW_HOLDER_TYPE
            is TimeItem -> TIME_VIEW_HOLDER_TYPE
        }
    }

    inner class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.time_text_view)

        fun bind(position: Int) {
            with (items[position] as TimeItem) {
                textView.text = time.timeToString(TIME_FORMAT)
                if (isSelected) {
                    textView.setTextColor(dynamicTimeColor)
                } else {
                    textView.setTextColor(timeColor)
                }
            }
        }
    }

    inner class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dynamicTimeView: DynamicTimeView = itemView.findViewById(R.id.dynamic_time_view)

        fun bind(position: Int) {
            if ((items[position] as EmptyTimeItem).startTime != null) {
                dynamicTimeView.startTime = (items[position] as EmptyTimeItem).startTime
            } else {
                dynamicTimeView.startTime = null
            }
            if ((items[position] as EmptyTimeItem).endTime != null) {
                dynamicTimeView.endTime = (items[position] as EmptyTimeItem).endTime
            } else {
                dynamicTimeView.endTime = null
            }
        }
    }

    companion object {
        private const val EMPTY_VIEW_HOLDER_TYPE = 1
        private const val TIME_VIEW_HOLDER_TYPE = 2
        private const val TIME_FORMAT = "HH:mm"
    }
}
