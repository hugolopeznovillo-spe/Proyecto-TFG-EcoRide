package com.ecoride.app.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoride.app.data.api.models.VehicleDto
import com.ecoride.app.data.repository.RentalRepository
import com.ecoride.app.data.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(val vehicle: VehicleDto) : DetailUiState
    data class Error(val message: String) : DetailUiState
}

sealed interface RentalAction {
    object Idle    : RentalAction
    object Loading : RentalAction
    data class Success(val message: String) : RentalAction
    data class Error(val message: String)   : RentalAction
}

class VehicleDetailViewModel(
    private val vehicleRepository: VehicleRepository,
    private val rentalRepository: RentalRepository
) : ViewModel() {

    private val _detailState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailState: StateFlow<DetailUiState> = _detailState.asStateFlow()

    private val _rentalAction = MutableStateFlow<RentalAction>(RentalAction.Idle)
    val rentalAction: StateFlow<RentalAction> = _rentalAction.asStateFlow()

    fun loadVehicle(id: String) {
        viewModelScope.launch {
            _detailState.value = DetailUiState.Loading
            vehicleRepository.getVehicleDetail(id)
                .onSuccess { _detailState.value = DetailUiState.Success(it) }
                .onFailure { _detailState.value = DetailUiState.Error(it.message ?: "Error") }
        }
    }

    fun startRental(vehicleId: String) {
        viewModelScope.launch {
            _rentalAction.value = RentalAction.Loading
            rentalRepository.startRental(vehicleId)
                .onSuccess { _rentalAction.value = RentalAction.Success(it) }
                .onFailure { _rentalAction.value = RentalAction.Error(it.message ?: "Error") }
        }
    }

    fun resetRentalAction() { _rentalAction.value = RentalAction.Idle }
}
