package com.ecoride.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GreenPrimary = Color(0xFF2E7D32)
private val GreenContainer = Color(0xFFC8E6C9)

private val LightColors = lightColorScheme(
    primary          = GreenPrimary,
    primaryContainer = GreenContainer,
    secondary        = Color(0xFF558B2F),
    tertiary         = Color(0xFF1565C0)
)

@Composable
fun EcoRideTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content     = content
    )
}
