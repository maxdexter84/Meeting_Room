package com.example.core_module.utils

import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun LocalTime.timeToString(format: String = "HH:mm"): String {
    val formatter = DateTimeFormatter.ofPattern(format)
    return this.format(formatter)
}

fun String.stringToTime(format: String = "HH:mm"): LocalTime {
    val formatter = DateTimeFormatter.ofPattern(format)
    return LocalTime.parse(this, formatter)
}
