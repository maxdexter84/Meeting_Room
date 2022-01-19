package com.meetingroom.feature_meet_now.presentation.available_now_fragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.core_module.utils.minutesToTimeString
import com.meetingroom.feature_meet_now.domain.entity.Room
import com.meetingroom.feature_meet_now_screen.R
import com.meetingroom.feature_meet_now_screen.databinding.RoomAvailableSoonItemBinding

class RoomsAvailableNowAdapter(
    private val rooms: MutableList<Room>,
    private val onRoomClicked: (Room) -> Unit
) :
    RecyclerView.Adapter<RoomsAvailableNowAdapter.RoomViewHolder>() {

    inner class RoomViewHolder(private val binding: RoomAvailableSoonItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(room: Room) {
            with(binding) {
                roomTitle.text = room.title
                roomInfo.text = itemView.resources.getString(
                    R.string.available_for,
                    room.timeUntilNextEvent?.minutesToTimeString()
                )
                roomColor.setBackgroundColor(Color.parseColor(room.color))
                rootLayout.setOnClickListener {
                    onRoomClicked(room)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        return RoomViewHolder(
            RoomAvailableSoonItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(rooms[position])
    }

    override fun getItemCount(): Int = rooms.size

    fun setData(newRooms: List<Room>) {
        rooms.clear()
        rooms.addAll(newRooms)
        notifyDataSetChanged()
    }
}