package com.example.gettickets.model

import java.time.*

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val datetime: LocalDateTime,
    val location: String,
    val isBookmarked: Boolean = false
)