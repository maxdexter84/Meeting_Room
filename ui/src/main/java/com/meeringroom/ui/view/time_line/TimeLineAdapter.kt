package com.meeringroom.ui.view.time_line

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meetingroom.ui.R

class TimeLineAdapter(private val items: List<String>): RecyclerView.Adapter<TimeLineAdapter.TimeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.time_line_item, parent, false)
        return TimeViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.textView.text = items[position]
    }

    override fun getItemCount() = items.size

    class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.time_text_view)
    }
}
