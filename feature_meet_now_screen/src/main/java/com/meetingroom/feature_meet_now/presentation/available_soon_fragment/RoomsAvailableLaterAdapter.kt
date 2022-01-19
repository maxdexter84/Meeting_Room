package com.meetingroom.feature_meet_now.presentation.available_soon_fragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.meetingroom.feature_meet_now.domain.entity.Room
import com.meetingroom.feature_meet_now_screen.databinding.RoomAvailableLaterItemBinding

class RoomsAvailableLaterAdapter(
    private val rooms: MutableList<Room>,
    private val onRoomClicked: (Room) -> Unit
) :
    RecyclerView.Adapter<RoomsAvailableLaterAdapter.RoomViewHolder>() {

    inner class RoomViewHolder(private val binding: RoomAvailableLaterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(room: Room) {
            with(binding) {
                roomTitle.text = room.title
                startTime.text = room.currentEventEndTime.dropLast(3)
                if (room.nextEventStartTime != null) {
                    endTime.text = room.nextEventStartTime.dropLast(3)
                } else {
                    untilText.isVisible = false
                    endTime.isVisible = false
                }
                roomColor.setBackgroundColor(Color.parseColor(room.color))
                rootLayout.setOnClickListener {
                    onRoomClicked(room)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        return RoomViewHolder(
            RoomAvailableLaterItemBinding.inflate(
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