package com.meeringroom.ui.view.time_line

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meetingroom.ui.R

class TimeLineAdapter(private val items: List<String?>, var hourHeight: Int, var textHeight: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            null -> EMPTY_VIEW_HOLDER_TYPE
            else -> TIME_VIEW_HOLDER_TYPE
        }

    }

   inner class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.time_text_view)

        fun bind(position: Int) {
            textView.text = items[position]
        }
    }

    inner class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view: View = itemView.findViewById(R.id.empty_view)

        fun bind(position: Int) {
            if (position == items.size - 1) {
                itemView.layoutParams = ViewGroup.LayoutParams(0, hourHeight)
            } else {
                itemView.layoutParams = ViewGroup.LayoutParams(0, hourHeight - textHeight)
            }
        }
    }

    companion object {
        private const val EMPTY_VIEW_HOLDER_TYPE = 1
        private const val TIME_VIEW_HOLDER_TYPE = 2
    }
}
