package com.meeringroom.ui.view.indicator_view

import kotlin.math.sqrt

object IndicatorUtils {
    fun isPointInCircle(
        x: Float,
        y: Float,
        cx: Float,
        cy: Float,
        radius: Float
    ): Boolean {
        return sqrt((x - cx) * (x - cx) + (y - cy) * (y - cy)) < radius
    }

    fun getTimePeriod(startTime: String, endTime:String): Int {
        val (startHour, startMinute) = startTime.split(":")
        val (endHour, endMinute) = endTime.split(":")
        val h = endHour.toInt() - startHour.toInt()
        var res = 0
        res = if (h > 0) {
            h * 60 - startMinute.toInt() + endMinute.toInt()
        } else endMinute.toInt() - startMinute.toInt()
        return res
    }
}