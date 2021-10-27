package com.meeringroom.ui.view.time_line

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meetingroom.ui.R

class TimeLineAdapter(private val items: List<String?>): RecyclerView.Adapter<TimeLineAdapter.TimeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.time_line_item, parent, false)
        return TimeViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        when (holder.itemViewType) {
            EMPTY_VIEW_HOLDER_TYPE -> {}
            TIME_VIEW_HOLDER_TYPE -> holder.textView.text = items[position]
        }

    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            null -> EMPTY_VIEW_HOLDER_TYPE
            else -> TIME_VIEW_HOLDER_TYPE
        }

    }

    class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.time_text_view)
    }

    class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.time_text_view)
    }

    companion object {
        private const val EMPTY_VIEW_HOLDER_TYPE = 1
        private const val TIME_VIEW_HOLDER_TYPE = 2
    }
}
