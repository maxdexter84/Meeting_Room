package com.example.core_module.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.dateToString(format: String = "EEE, d MMM"): String {
    val formatter = DateTimeFormatter.ofPattern(format)
    return this.format(formatter)
}

fun String.stringToDate(format: String = "EEE, d MMM"): LocalDate {
    val formatter = DateTimeFormatter.ofPattern(format)
    return LocalDate.parse(this, formatter)
}