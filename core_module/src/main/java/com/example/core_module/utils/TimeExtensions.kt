package com.example.core_module.utils

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

fun LocalTime.roundUpMinute(): LocalTime {
    return withMinute((ceil(minute / 5.0) * 5).toInt())
}
