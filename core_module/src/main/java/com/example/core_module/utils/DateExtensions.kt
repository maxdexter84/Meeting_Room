package com.example.core_module.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun LocalDate.dateToString(format: String): String {
    val formatter = DateTimeFormatter.ofPattern(format, Locale.UK)
    return this.format(formatter)
}

fun String.stringToDate(format: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern(format, Locale.UK)
    return LocalDate.parse(this, formatter)
}
