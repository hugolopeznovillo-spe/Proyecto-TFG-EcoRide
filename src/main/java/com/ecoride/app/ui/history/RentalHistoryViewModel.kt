package com.ecoride.app.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoride.app.data.api.models.RentalDto
import com.ecoride.app.data.repository.RentalRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface HistoryUiState {
    object Loading : HistoryUiState
    data class Success(val rentals: List<RentalDto>) : HistoryUiState
    data class Error(val message: String) : HistoryUiState
}

class RentalHistoryViewModel(private val rentalRepository: RentalRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    // Ticker para actualizaciones en tiempo real (cada segundo)
    val currentTime = flow {
        while (true) {
            emit(System.currentTimeMillis())
            delay(1000)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = System.currentTimeMillis()
    )

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

    fun endRental(rentalId: String) {
        viewModelScope.launch {
            rentalRepository.endRental(rentalId)
                .onSuccess { loadHistory() }
        }
    }
}
