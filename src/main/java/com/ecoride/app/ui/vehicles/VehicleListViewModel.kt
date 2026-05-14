package com.ecoride.app.ui.vehicles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoride.app.data.local.VehicleEntity
import com.ecoride.app.data.repository.AuthRepository
import com.ecoride.app.data.repository.VehicleRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

import com.ecoride.app.data.api.models.RentalDto
import com.ecoride.app.data.repository.RentalRepository
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.Duration

data class VehicleListUiState(
    val vehicles: List<VehicleEntity> = emptyList(),
    val isLoading: Boolean            = false,
    val errorMessage: String?         = null,
    val username: String              = "",
    val activeRental: RentalDto?      = null,
    val elapsedSeconds: Long          = 0L,
    val currentCost: Double           = 0.0
)

class VehicleListViewModel(
    private val vehicleRepository: VehicleRepository,
    private val authRepository: AuthRepository,
    private val rentalRepository: RentalRepository
) : ViewModel() {

    private val _isLoading    = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _activeRental = MutableStateFlow<RentalDto?>(null)

    // Ticker que emite el tiempo actual cada segundo para forzar la actualización de la UI
    private val _ticker = flow {
        while (true) {
            emit(System.currentTimeMillis())
            delay(1000)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), System.currentTimeMillis())

    // Combinación de flujos para generar un UI State siempre actualizado
    val uiState: StateFlow<VehicleListUiState> = combine(
        vehicleRepository.getCachedVehicles(),
        _isLoading,
        _errorMessage,
        authRepository.usernameFlow(),
        combine(_activeRental, _ticker) { active, now -> active to now }
    ) { vehicles, loading, error, username, rentalInfo ->
        
        val active = rentalInfo.first
        val nowMillis = rentalInfo.second
        
        var elapsed = 0L
        var cost = 0.0
        
        active?.startTime?.let { startTimeStr ->
            try {
                // Normalizamos el formato de fecha del servidor a ISO-8601 UTC
                val cleanTime = startTimeStr.replace(" ", "T").let {
                    when {
                        it.length == 16 -> "$it:00Z"
                        it.length == 19 -> "${it}Z"
                        !it.endsWith("Z") && !it.contains("+") -> "${it}Z"
                        else -> it
                    }
                }
                val start = Instant.parse(cleanTime)
                val now = Instant.ofEpochMilli(nowMillis)
                val diff = Duration.between(start, now)
                
                elapsed = diff.seconds.coerceAtLeast(0)
                cost = (elapsed / 60.0) * (active.pricePerMin ?: 0.0)
            } catch (e: Exception) {
                // Si falla el parseo, se mantiene en 0
            }
        }

        VehicleListUiState(
            vehicles       = vehicles,
            isLoading      = loading,
            errorMessage   = error,
            username       = username ?: "",
            activeRental   = active,
            elapsedSeconds = elapsed,
            currentCost    = cost
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
            
            rentalRepository.getActiveRental()
                .onSuccess { _activeRental.value = it }
            _isLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch { authRepository.logout() }
    }
}
