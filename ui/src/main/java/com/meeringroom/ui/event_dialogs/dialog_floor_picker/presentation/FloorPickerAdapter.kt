package com.meeringroom.ui.event_dialogs.dialog_floor_picker.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.meeringroom.ui.event_dialogs.dialog_floor_picker.model.FloorData
import com.meeringroom.ui.view_utils.onClick
import com.meetingroom.ui.R
import com.meetingroom.ui.databinding.FloorItemBinding

class FloorPickerAdapter(private val onItemSelected: (FloorData) -> Unit) :
    ListAdapter<FloorData, FloorPickerAdapter.ViewHolder>(Comparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            FloorItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: FloorItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(floor: FloorData) {
            with(binding.rbFloorPicker) {
                text = itemView.resources.getString(
                    R.string.floor_item_title, floor.floorName, floor.numberOfPlaces
                )
                isSelected = floor.isSelected
                isChecked = floor.isSelected
                onClick {
                    onItemSelected(floor)
                }
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<FloorData>() {
        override fun areItemsTheSame(oldItem: FloorData, newItem: FloorData) =
            oldItem.floorName == newItem.floorName

        override fun areContentsTheSame(oldItem: FloorData, newItem: FloorData) =
            oldItem == newItem
    }
}