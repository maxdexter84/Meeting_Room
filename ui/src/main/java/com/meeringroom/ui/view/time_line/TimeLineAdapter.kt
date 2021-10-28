package com.meeringroom.ui.view.time_line

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meetingroom.ui.R

class TimeLineAdapter(private val items: List<String?>, var hourHeight: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        hourHeight -= TEXT_HEIGHT
    }

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
            EMPTY_VIEW_HOLDER_TYPE -> {}
            TIME_VIEW_HOLDER_TYPE -> (holder as TimeViewHolder).textView.text = items[position]
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            null -> EMPTY_VIEW_HOLDER_TYPE
            else -> TIME_VIEW_HOLDER_TYPE
        }

    }

   inner class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.time_text_view)
    }

    inner class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view: View = itemView.findViewById(R.id.empty_view)
        init {
            itemView.layoutParams = ViewGroup.LayoutParams(0, hourHeight)
        }
    }

    companion object {
        private const val EMPTY_VIEW_HOLDER_TYPE = 1
        private const val TIME_VIEW_HOLDER_TYPE = 2
        private const val TEXT_HEIGHT = 16
    }
}
