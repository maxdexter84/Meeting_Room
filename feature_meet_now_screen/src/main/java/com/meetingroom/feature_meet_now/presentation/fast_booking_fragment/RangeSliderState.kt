package com.meetingroom.feature_meet_now.presentation.fast_booking_fragment

data class RangeSliderState(
    val startLimit: Int,
    val endLimit: Int,
    var startSelected: Int,
    var endSelected: Int
)