package com.meetingroom.feature_meet_now.presentation.available_now_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meetingroom.feature_meet_now.domain.entity.Room
import com.meetingroom.feature_meet_now_screen.databinding.AvailableRoomItemBinding

class RoomsAvailableNowAdapter(private val rooms: MutableList<Room>, private val onRoomClicked: (Room) -> Unit) :
    RecyclerView.Adapter<RoomsAvailableNowAdapter.RoomViewHolder>() {

    inner class RoomViewHolder(private val binding: AvailableRoomItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(room: Room) {
            with(binding) {
                roomTitle.text = room.title
                roomColor.setBackgroundColor(room.color)
                rootLayout.setOnClickListener {
                    onRoomClicked(room)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        return RoomViewHolder(
            AvailableRoomItemBinding.inflate(
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