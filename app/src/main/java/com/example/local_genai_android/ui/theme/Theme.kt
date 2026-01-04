package com.example.local_genai_android.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
        primary = Purple80,
        secondary = PurpleGrey80,
        tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
        primary = Purple40,
        secondary = PurpleGrey40,
        tertiary = Pink40

        /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)
val LocalCustomColors = staticCompositionLocalOf { CustomColors() }

@Immutable
data class CustomColors(
    val appTitleGradientColors: List<Color> = listOf(),
    val tabHeaderBgColor: Color = Color.Transparent,
    val taskCardBgColor: Color = Color.Transparent,
    val taskBgColors: List<Color> = listOf(),
    val taskBgGradientColors: List<List<Color>> = listOf(),
    val taskIconColors: List<Color> = listOf(),
    val taskIconShapeBgColor: Color = Color.Transparent,
    val homeBottomGradient: List<Color> = listOf(),
    val userBubbleBgColor: Color = Color.Transparent,
    val agentBubbleBgColor: Color = Color.Transparent,
    val linkColor: Color = Color.Transparent,
    val successColor: Color = Color.Transparent,
    val recordButtonBgColor: Color = Color.Transparent,
    val waveFormBgColor: Color = Color.Transparent,
    val modelInfoIconColor: Color = Color.Transparent,
    val warningContainerColor: Color = Color.Transparent,
    val warningTextColor: Color = Color.Transparent,
    val errorContainerColor: Color = Color.Transparent,
    val errorTextColor: Color = Color.Transparent,
)

val MaterialTheme.customColors: CustomColors
    @Composable @ReadOnlyComposable get() = LocalCustomColors.current

@Composable
fun LocalgenaiandroidTheme(
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

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
    )
}