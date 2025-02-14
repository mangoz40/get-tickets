package com.example.gettickets.model

data class BookingRequest(
    val eventId: Int,
    val fullName: String,
    val email: String,
    val numberOfTickets: Int,
    val totalAmount: Double
)