package com.andersen.feature_rooms_screen.domain.entity.remote

import com.google.gson.annotations.SerializedName

data class RoomsEventDTO(

	@field:SerializedName("color")
	val color: String,

	@field:SerializedName("projector")
	val projector: Boolean,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("office")
	val office: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("floor")
	val floor: Int,

	@field:SerializedName("board")
	val board: Boolean,

	@field:SerializedName("events")
	val events: List<EventsItem>,

	@field:SerializedName("capacity")
	val capacity: Int
)

data class EventsItem(

	@field:SerializedName("userSkype")
	val userSkype: String,

	@field:SerializedName("color")
	val color: String,

	@field:SerializedName("userFullName")
	val userFullName: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("endDateTime")
	val endDateTime: String,

	@field:SerializedName("userId")
	val userId: Int,

	@field:SerializedName("roomId")
	val roomId: Int,

	@field:SerializedName("room")
	val room: String,

	@field:SerializedName("startDateTime")
	val startDateTime: String,

	@field:SerializedName("userPosition")
	val userPosition: String,

	@field:SerializedName("userEmail")
	val userEmail: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("status")
	val status: String?
)
