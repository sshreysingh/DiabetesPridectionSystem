package com.diabetes.prediction.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary            = MedicalBluePrimary,
    onPrimary          = Color.White,
    primaryContainer   = MedicalBlueBg,
    onPrimaryContainer = MedicalBlueDark,
    secondary            = AccentTeal,
    onSecondary          = Color.White,
    secondaryContainer   = SuccessGreenBg,
    onSecondaryContainer = SuccessGreen,
    background         = BackgroundLight,
    onBackground       = TextPrimaryDark,
    surface            = SurfaceWhite,
    onSurface          = TextPrimaryDark,
    surfaceVariant     = BackgroundLight,
    onSurfaceVariant   = TextSecondaryDark,
    outline            = BorderLight,
    error              = WarningRed,
    onError            = Color.White,
    errorContainer     = WarningRedBg,
    onErrorContainer   = WarningRed
)

@Composable
fun DiabetesPredictionTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            
            // Set dark icons for the status bar on our light theme
            val windowInsetsController = WindowCompat.getInsetsController(window, view)
            windowInsetsController.isAppearanceLightStatusBars = true
            windowInsetsController.isAppearanceLightNavigationBars = true
            
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography  = Typography, // Reusing existing Type.kt
        content     = content
    )
}
