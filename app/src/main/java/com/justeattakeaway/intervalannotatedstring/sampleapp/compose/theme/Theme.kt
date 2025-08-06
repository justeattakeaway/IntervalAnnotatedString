package com.justeattakeaway.intervalannotatedstring.sampleapp.compose.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Colors.primary,
    onPrimary = Colors.onPrimary,
    secondary = Colors.secondary,
    onSecondary = Colors.onSecondary,
    secondaryContainer = Colors.secondaryContainer,
    onSecondaryContainer = Colors.onSecondaryContainer,

    surface = Colors.surface,
    onSurface = Colors.onSurface,
    surfaceContainer = Colors.surfaceContainer,
    onSurfaceVariant = Colors.onSurfaceVariant,
    surfaceContainerHighest = Colors.surfaceContainerHighest,

    error = Colors.error,
    onError = Colors.onError,
    errorContainer = Colors.errorContainer,
    onErrorContainer = Colors.onErrorContainer,

    outline = Colors.outline,
)

@Composable
fun SampleAppTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content,
    )
}
