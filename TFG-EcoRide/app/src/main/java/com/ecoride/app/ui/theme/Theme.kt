package com.ecoride.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Colores base del logo
val EcoGreen = Color(0xFFB0E64C)        // Verde Lima (Brillante)
val EcoGreenDark = Color(0xFF4A6B00)    // Verde Oscuro (Para texto y contraste)
val EcoBlue = Color(0xFF1E88A1)         // Azul Cian ajustado para contraste
val EcoBlack = Color(0xFF1A1A1A)        // Negro Antracita
val EcoWhite = Color(0xFFFFFFFF)
val EcoGrayLight = Color(0xFFF2F2F2)    // Gris muy claro para tarjetas

private val LightColors = lightColorScheme(
    primary = EcoGreenDark,             // Usamos el verde oscuro como primario para que el texto sea legible
    onPrimary = EcoWhite,
    primaryContainer = EcoGreen,        // El verde brillante para contenedores y botones grandes
    onPrimaryContainer = EcoBlack,
    
    secondary = EcoBlue,
    onSecondary = EcoWhite,
    secondaryContainer = Color(0xFFD1F5FD),
    
    tertiary = EcoBlack,
    onTertiary = EcoWhite,
    
    background = EcoWhite,
    onBackground = EcoBlack,
    surface = EcoGrayLight,             // Superficie grisácea clara para que las tarjetas resalten
    onSurface = EcoBlack,
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = EcoBlack
)

private val DarkColors = darkColorScheme(
    primary = EcoGreen,
    onPrimary = EcoBlack,
    secondary = EcoBlue,
    onSecondary = EcoBlack,
    tertiary = EcoWhite,
    background = EcoBlack,
    surface = Color(0xFF2C2C2C),
    onSurface = EcoWhite
)

@Composable
fun EcoRideTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
