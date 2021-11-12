package com.meeringroom.ui.view.time_line

import java.time.LocalTime

sealed class TimeLineItem

data class TimeItem(val time: LocalTime, var isSelected: Boolean): TimeLineItem()
data class EmptyTimeItem(var startTime: LocalTime?, var endTime: LocalTime?): TimeLineItem()