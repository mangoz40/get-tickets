package com.example.gettickets.model

import java.time.*

data class Event(
    val id: Int,
    val title: String,
    val description: String?,
    val location: String?,
    //val isBookmarked: Boolean = false
    val available_tickets: Int = 4,
    val price: Double = 0.0,
    val date: String,
    val created_at: String,
    val updated_at: String
)