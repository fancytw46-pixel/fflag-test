package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BloxBoostColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color(0xFF003831),
    primaryContainer = Color(0xFF004D40),
    onPrimaryContainer = Color(0xFF70FEE4),
    secondary = ElectricViolet,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF3B1E6D),
    onSecondaryContainer = Color(0xFFE9D8FD),
    tertiary = HyperGreen,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF064E3B),
    onTertiaryContainer = Color(0xFFA7F3D0),
    background = CyberDark,
    onBackground = TextPrimary,
    surface = CyberSurface,
    onSurface = TextPrimary,
    surfaceVariant = CyberSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = CyberBorder,
    outlineVariant = Color(0xFF273449),
    error = CrimsonAlert,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BloxBoostColorScheme,
        typography = Typography,
        content = content
    )
}
