package com.example.controleitens.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFCBA6F7),
    onPrimary = Color(0xFF1E1E2E),

    primaryContainer = Color(0xFF45475A),
    onPrimaryContainer = Color(0xFFCDD6F4),

    background = Color(0xFF1E1E2E),
    onBackground = Color(0xFFCDD6F4),

    surface = Color(0xFF181825),
    onSurface = Color(0xFFCDD6F4),

    surfaceVariant = Color(0xFF313244),
    onSurfaceVariant = Color(0xFFA6ADC8),

    error = Color(0xFFF38BA8),
    onError = Color(0xFF1E1E2E)
)

@Composable
fun ControleItensTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}