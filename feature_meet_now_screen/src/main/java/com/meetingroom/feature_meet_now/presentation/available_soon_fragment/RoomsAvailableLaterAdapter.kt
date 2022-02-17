package com.meetingroom.feature_meet_now.presentation.available_soon_fragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.core_module.utils.TimeUtilsConstants.TIME_FORMAT
import com.example.core_module.utils.roundUpMinute
import com.example.core_module.utils.timeToString
import com.meetingroom.feature_meet_now.domain.entity.Room
import com.meetingroom.feature_meet_now.presentation.fast_booking_fragment.Constants.MAX_EVENT_TIME
import com.meetingroom.feature_meet_now.presentation.fast_booking_fragment.Constants.STEP
import com.meetingroom.feature_meet_now.presentation.utils.getValidTimeUntilNextEvent
import com.meetingroom.feature_meet_now_screen.databinding.AllRoomsAreOccupiedPlaceholderBinding
import com.meetingroom.feature_meet_now_screen.databinding.RoomAvailableLaterItemBinding
import java.time.LocalTime
import java.time.ZoneOffset

private const val HEADER_TYPE = 0
private const val ROOM_TYPE = 1
private const val ADDITIONAL_VIEW_HOLDER_POSITION = 1

class RoomsAvailableLaterAdapter(
    private val rooms: MutableList<Room>,
    private val onRoomClicked: (Room) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class HeaderMessageViewHolder(binding: AllRoomsAreOccupiedPlaceholderBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class RoomViewHolder(private val binding: RoomAvailableLaterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(room: Room) {
            with(binding) {
                roomTitle.text = room.title
                if (room.availableIn != null) {
                    startTime.text =
                        LocalTime.now(ZoneOffset.UTC).plusMinutes(room.availableIn.toLong())
                            .roundUpMinute(STEP).timeToString(TIME_FORMAT)
                }
                endTime.text =
                    LocalTime.now(ZoneOffset.UTC)
                        .plusMinutes(room.getValidTimeUntilNextEvent(MAX_EVENT_TIME).toLong())
                        .roundUpMinute(STEP).timeToString(TIME_FORMAT)
                roomColor.setBackgroundColor(Color.parseColor(room.color))
                rootLayout.setOnClickListener {
                    onRoomClicked(room)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == HEADER_TYPE) HEADER_TYPE
        else ROOM_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == HEADER_TYPE)
            HeaderMessageViewHolder(
                AllRoomsAreOccupiedPlaceholderBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        else RoomViewHolder(
            RoomAvailableLaterItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RoomViewHolder) holder.bind(rooms[position - ADDITIONAL_VIEW_HOLDER_POSITION])
    }

    override fun getItemCount(): Int = rooms.size + ADDITIONAL_VIEW_HOLDER_POSITION

    fun setData(newRooms: List<Room>) {
        rooms.clear()
        rooms.addAll(newRooms)
        notifyDataSetChanged()
    }
}