package com.example.gettickets.ui.state

sealed class EventUiState {
    data object Loading : EventUiState()
    data class Success(val events: Any?) : EventUiState()
    data class Error(val message: String) : EventUiState()
}