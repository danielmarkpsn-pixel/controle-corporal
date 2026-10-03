package com.danielmarkpsn.controlecorporal.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = RosaMedio,
    onPrimary = FundoClaro,
    primaryContainer = RosaClaro,
    onPrimaryContainer = RosaEscuro,
    secondary = LilasMedio,
    onSecondary = FundoClaro,
    secondaryContainer = LilasClaro,
    onSecondaryContainer = LilasEscuro,
    background = FundoClaro,
    onBackground = TextoClaro,
    surface = SuperficieClara,
    onSurface = TextoClaro
)

private val DarkColors = darkColorScheme(
    primary = RosaClaro,
    onPrimary = RosaEscuro,
    primaryContainer = RosaEscuro,
    onPrimaryContainer = RosaClaro,
    secondary = LilasClaro,
    onSecondary = LilasEscuro,
    secondaryContainer = LilasEscuro,
    onSecondaryContainer = LilasClaro,
    background = FundoEscuro,
    onBackground = TextoEscuro,
    surface = SuperficieEscura,
    onSurface = TextoEscuro
)

@Composable
fun ControleCorporalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
