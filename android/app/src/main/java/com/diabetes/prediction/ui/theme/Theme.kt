package com.diabetes.prediction.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary            = Teal500,
    onPrimary          = Color(0xFF001F24),
    primaryContainer   = Teal600,
    onPrimaryContainer = Teal300,
    secondary            = Purple500,
    onSecondary          = Color.White,
    secondaryContainer   = Purple600,
    onSecondaryContainer = Purple300,
    tertiary           = Green500,
    onTertiary         = Color.White,
    background         = DeepNavy,
    onBackground       = TextPrimary,
    surface            = Navy800,
    onSurface          = TextPrimary,
    surfaceVariant     = Navy700,
    onSurfaceVariant   = TextSecondary,
    outline            = CardBorder,
    error              = Red500,
    onError            = Color.White,
    errorContainer     = Red600,
    onErrorContainer   = Red300,
)

@Composable
fun DiabetesPredictionTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor     = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography  = Typography,
        content     = content
    )
}
