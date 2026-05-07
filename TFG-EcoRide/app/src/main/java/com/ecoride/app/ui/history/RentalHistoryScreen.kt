package com.ecoride.app.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ecoride.app.data.api.models.RentalDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentalHistoryScreen(
    viewModel: RentalHistoryViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis alquileres") },
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
            when (val state = uiState) {
                is HistoryUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is HistoryUiState.Error -> {
                    Column(
                        modifier            = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.ErrorOutline, null, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(8.dp))
                        Text(state.message)
                        Spacer(Modifier.height(8.dp))
                        TextButton(onClick = { viewModel.loadHistory() }) { Text("Reintentar") }
                    }
                }
                is HistoryUiState.Success -> {
                    if (state.rentals.isEmpty()) {
                        Box(
                            modifier         = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.History, null, modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(Modifier.height(8.dp))
                                Text("Aún no tienes alquileres")
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding      = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = state.rentals,
                                key   = { it.id }
                            ) { rental ->
                                RentalCard(rental = rental)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RentalCard(rental: RentalDto) {
    val isActive = rental.status == "activo"

    Card(
        modifier  = Modifier.fillMaxWidth(),
        colors    = CardDefaults.cardColors(
            containerColor = if (isActive)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text  = rental.vehicleModel,
                    style = MaterialTheme.typography.titleMedium
                )
                AssistChip(
                    onClick = {},
                    label   = { Text(if (isActive) "Activo" else "Finalizado") },
                    leadingIcon = {
                        Icon(
                            if (isActive) Icons.Default.DirectionsRun else Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text  = "Inicio: ${rental.startTime.take(16).replace("T", " ")}",
                style = MaterialTheme.typography.bodySmall
            )

            if (!isActive) {
                rental.endTime?.let {
                    Text(
                        text  = "Fin: ${it.take(16).replace("T", " ")}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row(
                    modifier              = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rental.durationMin?.let {
                        Text(
                            "%.1f min".format(it),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    rental.totalCost?.let {
                        Text(
                            "%.2f €".format(it),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
