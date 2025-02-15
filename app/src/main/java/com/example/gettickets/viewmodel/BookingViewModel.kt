package com.example.gettickets.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gettickets.repository.EventRepository
import com.example.gettickets.ui.state.BookingUiState
import com.example.gettickets.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<BookingUiState>(BookingUiState.Loading)
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadEvent(eventId: Int) {
        viewModelScope.launch {
            repository.getEventById(eventId).collect { result ->
                _uiState.value = when (result) {
                    is Resource.Success -> BookingUiState.Success(result.data)
                    is Resource.Error -> BookingUiState.Error(result.message)
                    is Resource.Loading -> BookingUiState.Loading
                }
            }
        }
    }
}

