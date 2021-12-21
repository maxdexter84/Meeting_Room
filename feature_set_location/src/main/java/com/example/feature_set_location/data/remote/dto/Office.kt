package com.example.feature_set_location.data.remote.dto

data class Office(
    val city: String,
    val country: String,
    val id: Int,
    val rooms: List<String>
)