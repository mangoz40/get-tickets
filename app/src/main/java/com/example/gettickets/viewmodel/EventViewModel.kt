package com.example.gettickets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gettickets.repository.EventRepository
import com.example.gettickets.ui.state.EventUiState
import com.example.gettickets.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<EventUiState>(EventUiState.Loading)
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    init {
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            repository.getEvents().collect {
                _uiState.value = when (it) {
                    is Resource.Success<*> -> EventUiState.Success(it.data)
                    is Resource.Error<*> -> EventUiState.Error(it.message)
                    is Resource.Loading<*> -> EventUiState.Loading
                    else -> EventUiState.Loading
                }
            }
        }
    }
}
