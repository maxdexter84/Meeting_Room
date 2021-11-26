package com.example.core_module.utils

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

fun LocalTime.timeToString(format: String): String {
    val formatter = DateTimeFormatter.ofPattern(format)
    return this.format(formatter)
}

fun String.stringToTime(format: String): LocalTime {
    val formatter = DateTimeFormatter.ofPattern(format)
    return LocalTime.parse(this, formatter)
}

fun String.stringToDateTime(format: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern(format)
    return LocalDateTime.parse(this, formatter)
}

fun LocalTime.roundUpMinute(min: Int): LocalTime {
    val newMinute = (ceil(minute / min.toDouble()) * min).toInt()
    if (newMinute == 60) {
        return plusHours(1).withMinute(0)
    }
    return withMinute(newMinute)
}
