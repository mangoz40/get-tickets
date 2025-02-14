package com.example.gettickets.model

import java.time.LocalDateTime

data class Booking(
    val id: Int,
    val event_id: Int,
    val customer_name: String,
    val customer_email: String,
    val ticket_quantity: Int,
    val qr_code: String?,
    val created_at: LocalDateTime,
    val updated_at: LocalDateTime
)