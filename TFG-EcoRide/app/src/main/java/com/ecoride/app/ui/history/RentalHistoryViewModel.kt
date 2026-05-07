package com.ecoride.app.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoride.app.data.api.models.RentalDto
import com.ecoride.app.data.repository.RentalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface HistoryUiState {
    object Loading : HistoryUiState
    data class Success(val rentals: List<RentalDto>) : HistoryUiState
    data class Error(val message: String) : HistoryUiState
}

class RentalHistoryViewModel(private val rentalRepository: RentalRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _uiState.value = HistoryUiState.Loading
            rentalRepository.getMyHistory()
                .onSuccess { _uiState.value = HistoryUiState.Success(it) }
                .onFailure { _uiState.value = HistoryUiState.Error(it.message ?: "Error") }
        }
    }
}
