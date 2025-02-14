package com.example.gettickets.ui.state

import com.example.gettickets.model.BookingResponse

// BookingState.kt
sealed class BookApiState {
    object Idle : BookApiState()
    object Loading : BookApiState()
    data class Success(val response: BookingResponse) : BookApiState()
    data class Error(val message: String) : BookApiState()
}