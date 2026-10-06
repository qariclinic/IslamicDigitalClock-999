package com.example.islamicdigitalclock.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(primary = GreenPrimary, tertiary = GoldAccent)
private val DarkColors = darkColorScheme(
    primary = GreenLight,
    tertiary = GoldAccent,
    background = DarkBg,
    surface = DarkBg
)

@Composable
fun IslamicDigitalClockTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
