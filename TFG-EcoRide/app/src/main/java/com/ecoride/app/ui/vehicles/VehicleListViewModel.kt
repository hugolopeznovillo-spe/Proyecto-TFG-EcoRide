package com.ecoride.app.ui.vehicles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoride.app.data.local.VehicleEntity
import com.ecoride.app.data.repository.AuthRepository
import com.ecoride.app.data.repository.VehicleRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class VehicleListUiState(
    val vehicles: List<VehicleEntity> = emptyList(),
    val isLoading: Boolean            = false,
    val errorMessage: String?         = null,
    val username: String              = ""
)

class VehicleListViewModel(
    private val vehicleRepository: VehicleRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading    = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    // Combina el flujo de Room + estados de carga/error en un único UiState
    val uiState: StateFlow<VehicleListUiState> = combine(
        vehicleRepository.getCachedVehicles(),
        _isLoading,
        _errorMessage,
        authRepository.usernameFlow()
    ) { vehicles, loading, error, username ->
        VehicleListUiState(
            vehicles     = vehicles,
            isLoading    = loading,
            errorMessage = error,
            username     = username ?: ""
        )
    }.stateIn(
        scope         = viewModelScope,
        started       = SharingStarted.WhileSubscribed(5_000),
        initialValue  = VehicleListUiState(isLoading = true)
    )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value    = true
            _errorMessage.value = null
            vehicleRepository.refreshVehicles()
                .onFailure { _errorMessage.value = "Sin conexión. Mostrando datos locales." }
            _isLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch { authRepository.logout() }
    }
}
