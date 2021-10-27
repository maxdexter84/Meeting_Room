package com.meetingroom.andersen.feature_landing.time_for_notification_dialog.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meetingroom.andersen.feature_landing.databinding.RoomAndTimeAdapterItemBinding
import com.meetingroom.andersen.feature_landing.time_for_notification_dialog.model.TimePickerAdapterModel

class TimePickerAdapter(private val onItemSelected: (TimePickerAdapterModel) -> Unit) :
    RecyclerView.Adapter<TimePickerAdapter.TimePickerViewHolder>() {

    var time = arrayListOf<TimePickerAdapterModel>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            time.clear()
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimePickerViewHolder {
        return TimePickerViewHolder(
            RoomAndTimeAdapterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TimePickerViewHolder, position: Int) {
        holder.bind(time[position])
    }

    override fun getItemCount() = time.size

    inner class TimePickerViewHolder(private val binding: RoomAndTimeAdapterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(timePickerAdapterModel: TimePickerAdapterModel) {
            with(binding) {
                roomAndTimeName.text = timePickerAdapterModel.title
                root.setOnClickListener { userChosenTime(timePickerAdapterModel) }
                roomAndTimeRadioButton.setOnClickListener { userChosenTime(timePickerAdapterModel) }
                roomAndTimeRadioButton.isChecked = timePickerAdapterModel.isSelected
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun userChosenTime(timePickerAdapterModel: TimePickerAdapterModel) {
        onItemSelected(timePickerAdapterModel)
        notifyDataSetChanged()
    }
}
