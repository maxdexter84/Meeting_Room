package com.example.core_module.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.dateToString(format: String): String {
    val formatter = DateTimeFormatter.ofPattern(format)
    return this.format(formatter)
}

fun String.stringToDate(format: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern(format)
    return LocalDate.parse(this, formatter)
}

