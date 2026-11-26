package com.patitas.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val PatitasColorScheme = lightColorScheme(
    primary              = Principal,
    onPrimary            = Blanco,
    primaryContainer     = Claro,
    onPrimaryContainer   = Texto,
    secondary            = Pastel,
    onSecondary          = Texto,
    secondaryContainer   = Claro,
    onSecondaryContainer = Texto,
    tertiary             = Rosa,
    onTertiary           = TextoSobreRosa,
    tertiaryContainer    = Verde,
    onTertiaryContainer  = TextoSobreVerde,
    background           = Fondo,
    onBackground         = Texto,
    surface              = Blanco,
    onSurface            = Texto,
    surfaceVariant       = Claro,
    onSurfaceVariant     = TextoSecundario,
    outline              = Borde,
    error                = ErrorPatitas
)

// Radios de la guía: 14 dp (campos y botones) y 22 dp (tarjetas)
private val PatitasShapes = Shapes(
    small      = RoundedCornerShape(14.dp),
    medium     = RoundedCornerShape(14.dp),
    large      = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(22.dp)
)

@Composable
fun PatitasTheme(content: @Composable () -> Unit) {
    // Sin dynamicColor (Android 12+) para conservar siempre la identidad morada de Patitas.
    MaterialTheme(
        colorScheme = PatitasColorScheme,
        typography = PatitasTypography,
        shapes = PatitasShapes,
        content = content
    )
}
