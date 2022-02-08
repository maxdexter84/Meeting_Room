package com.meetingroom.feature_meet_now.presentation.fast_booking_fragment

data class RangeSliderState(
    val startLimit: Float,
    val endLimit: Float,
    var startSelected: Float,
    var endSelected: Float
)