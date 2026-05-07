package com.ecoride.app.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecoride.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface RegisterUiState {
    object Idle    : RegisterUiState
    object Loading : RegisterUiState
    object Success : RegisterUiState
    data class Error(val message: String) : RegisterUiState
}

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(username: String, email: String, password: String, confirm: String) {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            _uiState.value = RegisterUiState.Error("Completa todos los campos")
            return
        }
        if (password != confirm) {
            _uiState.value = RegisterUiState.Error("Las contraseñas no coinciden")
            return
        }
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading
            authRepository.register(username.trim(), email.trim(), password)
                .onSuccess { _uiState.value = RegisterUiState.Success }
                .onFailure { _uiState.value = RegisterUiState.Error(it.message ?: "Error desconocido") }
        }
    }

    fun resetState() { _uiState.value = RegisterUiState.Idle }
}
