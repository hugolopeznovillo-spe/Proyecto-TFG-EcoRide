package com.ecoride.app.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ecoride.app.ui.components.EcoRideButton
import com.ecoride.app.ui.components.InfoRow
import com.ecoride.app.ui.components.StatusChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailScreen(
    vehicleId: String,
    viewModel: VehicleDetailViewModel,
    onBack: () -> Unit
) {
    val detailState  by viewModel.detailState.collectAsStateWithLifecycle()
    val rentalAction by viewModel.rentalAction.collectAsStateWithLifecycle()

    var showSuccessDialog by remember { mutableStateOf(false) }
    var successMsg        by remember { mutableStateOf("") }

    LaunchedEffect(vehicleId) { viewModel.loadVehicle(vehicleId) }

    LaunchedEffect(rentalAction) {
        if (rentalAction is RentalAction.Success) {
            successMsg = (rentalAction as RentalAction.Success).message
            showSuccessDialog = true
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                viewModel.resetRentalAction()
            },
            icon    = { Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.primary) },
            title   = { Text("¡Alquiler iniciado!") },
            text    = { Text(successMsg) },
            confirmButton = {
                TextButton(onClick = {
                    showSuccessDialog = false
                    viewModel.resetRentalAction()
                    onBack()
                }) { Text("Aceptar") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del patinete") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = detailState) {
                is DetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DetailUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.ErrorOutline, null, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(8.dp))
                        Text(state.message)
                        Spacer(Modifier.height(8.dp))
                        TextButton(onClick = { viewModel.loadVehicle(vehicleId) }) {
                            Text("Reintentar")
                        }
                    }
                }
                is DetailUiState.Success -> {
                    val v = state.vehicle
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Cabecera
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Text(v.model, style = MaterialTheme.typography.headlineSmall)
                            StatusChip(status = v.status)
                        }

                        HorizontalDivider()

                        // Info
                        InfoRow(Icons.Default.BatteryStd,  "Batería",   "${v.battery}%")
                        InfoRow(Icons.Default.LocationOn,  "Ubicación", v.location)
                        InfoRow(Icons.Default.EuroSymbol,  "Precio",    "${v.pricePerMin} €/min")

                        Spacer(Modifier.weight(1f))

                        // Error de alquiler
                        if (rentalAction is RentalAction.Error) {
                            Text(
                                text  = (rentalAction as RentalAction.Error).message,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        // Botón solo si está disponible
                        if (v.status == "disponible") {
                            EcoRideButton(
                                text      = "Iniciar alquiler",
                                onClick   = { viewModel.startRental(v.id) },
                                isLoading = rentalAction is RentalAction.Loading
                            )
                        } else {
                            OutlinedButton(
                                onClick  = {},
                                enabled  = false,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    if (v.status == "en_uso") "En uso actualmente"
                                    else "En mantenimiento"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
