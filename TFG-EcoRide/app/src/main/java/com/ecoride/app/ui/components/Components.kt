package com.ecoride.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

// ── TextField genérico ────────────────────────────────────────────────────────

@Composable
fun EcoRideTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value                = value,
        onValueChange        = onValueChange,
        label                = { Text(label) },
        modifier             = Modifier.fillMaxWidth(),
        singleLine           = true,
        trailingIcon         = trailingIcon,
        keyboardOptions      = keyboardOptions,
        visualTransformation = visualTransformation
    )
}

// ── Botón con indicador de carga ──────────────────────────────────────────────

@Composable
fun EcoRideButton(
    text: String,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    enabled: Boolean   = true
) {
    Button(
        onClick  = onClick,
        enabled  = enabled && !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier  = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color     = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Text(text)
        }
    }
}

// ── Chip de estado de patinete ────────────────────────────────────────────────

@Composable
fun StatusChip(status: String) {
    val (label, containerColor) = when (status) {
        "disponible"   -> "Disponible"   to MaterialTheme.colorScheme.primaryContainer
        "en_uso"       -> "En uso"       to MaterialTheme.colorScheme.secondaryContainer
        "mantenimiento"-> "Mantenimiento" to MaterialTheme.colorScheme.errorContainer
        else           -> status          to MaterialTheme.colorScheme.surfaceVariant
    }
    SuggestionChip(
        onClick = {},
        label   = { Text(label, style = MaterialTheme.typography.labelSmall) },
        colors  = SuggestionChipDefaults.suggestionChipColors(containerColor = containerColor)
    )
}

// ── Fila de información ───────────────────────────────────────────────────────

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier          = Modifier.fillMaxWidth()
    ) {
        Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
