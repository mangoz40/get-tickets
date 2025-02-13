package com.example.gettickets.ui.state

import com.example.gettickets.model.Event

sealed class BookingUiState {
    data object Loading : BookingUiState()
    data class Success(val event: Event) : BookingUiState()
    data class Error(val message: String) : BookingUiState()
}