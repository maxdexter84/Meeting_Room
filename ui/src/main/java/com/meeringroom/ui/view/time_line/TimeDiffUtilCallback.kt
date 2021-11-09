package com.meeringroom.ui.view.time_line

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil


class TimeDiffUtilCallback(private val oldList: List<TimeLineItem>, private val newList: List<TimeLineItem>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    @SuppressLint("NewApi")
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return (oldItem is TimeItem && newItem is TimeItem && oldItem.time == newItem.time && oldItem.isSelected == newItem.isSelected)
                || (oldItem is EmptyTimeItem && newItem is EmptyTimeItem && oldItem.startTime == newItem.startTime && oldItem.endTime == newItem.endTime)
    }
}