package com.danielmarkpsn.controlecorporal.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AppColors = darkColorScheme(
    primary = Lilas,
    onPrimary = Branco,
    primaryContainer = LilasEscuro,
    onPrimaryContainer = Branco,
    secondary = LilasClaro,
    onSecondary = Preto,
    secondaryContainer = SuperficieElevada,
    onSecondaryContainer = LilasClaro,
    tertiary = Sucesso,
    onTertiary = Preto,
    background = Fundo,
    onBackground = Branco,
    surface = Superficie,
    onSurface = Branco,
    surfaceVariant = SuperficieElevada,
    onSurfaceVariant = TextoSecundario,
    outline = Divisor
)

@Composable
fun ControleCorporalTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Fundo.toArgb()
            window.navigationBarColor = Fundo.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }
    MaterialTheme(colorScheme = AppColors, typography = AppTypography, content = content)
}
