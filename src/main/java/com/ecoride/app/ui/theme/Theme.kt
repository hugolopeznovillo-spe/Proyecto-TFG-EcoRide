package com.ecoride.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Colores base personalizados
val EcoGreen = Color(0xFFB0E64C)        // Verde Lima
val EcoGreenDark = Color(0xFF385200)    // Verde más oscuro para legibilidad
val EcoBlue = Color(0xFF1E88A1)         // Azul
val EcoBlack = Color(0xFF121212)        // Negro Material
val EcoWhite = Color(0xFFFFFFFF)
val EcoGraySurface = Color(0xFF1C1B1F)  // Superficie oscura estándar M3

private val LightColors = lightColorScheme(
    primary = EcoGreenDark,
    onPrimary = EcoWhite,
    primaryContainer = EcoGreen,
    onPrimaryContainer = Color(0xFF101E00),
    secondary = EcoBlue,
    onSecondary = EcoWhite,
    secondaryContainer = Color(0xFFD1F5FD),
    onSecondaryContainer = Color(0xFF001F25),
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onBackground = EcoBlack,
    onSurface = EcoBlack,
)

private val DarkColors = darkColorScheme(
    primary = EcoGreen,
    onPrimary = Color(0xFF203600),
    primaryContainer = EcoGreenDark,
    onPrimaryContainer = EcoGreen,
    secondary = EcoBlue,
    onSecondary = Color(0xFF003642),
    background = EcoBlack,
    surface = EcoGraySurface,
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0)
)

@Composable
fun EcoRideTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
