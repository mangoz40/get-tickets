package com.example.gettickets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gettickets.api.BookingApiService
import com.example.gettickets.model.BookingRequest
import com.example.gettickets.model.BookingResponse
import com.example.gettickets.ui.state.BookApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookingApiService: BookingApiService
) : ViewModel() {
    private val _bookState = MutableStateFlow<BookApiState>(BookApiState.Idle)
    val bookState: StateFlow<BookApiState> = _bookState.asStateFlow()

    fun createBooking(
        eventId: Int,
        fullName: String,
        email: String,
        numberOfTickets: Int,
        totalAmount: Double
    ) {
        viewModelScope.launch {
            _bookState.value = BookApiState.Loading

            try {
                val bookingRequest = BookingRequest(
                    eventId = eventId,
                    fullName = fullName,
                    email = email,
                    numberOfTickets = numberOfTickets,
                    totalAmount = totalAmount
                )

                val response = bookingApiService.createBooking(bookingRequest)

                if (response.isSuccessful) {
                    response.body()?.let { bookingResponse ->
                        _bookState.value = BookApiState.Success(bookingResponse)
                    } ?: run {
                        _bookState.value = BookApiState.Error("Empty response from server")
                    }
                } else {
                    _bookState.value = BookApiState.Error("Booking failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _bookState.value = BookApiState.Error("Network error: ${e.message}")
            }
        }
    }
}

// BookingState.kt
sealed class BookingState {
    object Idle : BookingState()
    object Loading : BookingState()
    data class Success(val response: BookingResponse) : BookingState()
    data class Error(val message: String) : BookingState()
}