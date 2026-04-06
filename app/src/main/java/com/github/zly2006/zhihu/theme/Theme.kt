package com.github.zly2006.zhihu.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

// ============================================
// Material Design 3 配色方案
// ============================================

val LightColorScheme = lightColorScheme(
    primary = md_primary_light,
    onPrimary = md_on_primary_light,
    primaryContainer = md_primary_container_light,
    onPrimaryContainer = md_on_primary_container_light,
    secondary = md_secondary_light,
    onSecondary = md_on_secondary_light,
    secondaryContainer = md_secondary_container_light,
    onSecondaryContainer = md_on_secondary_container_light,
    tertiary = md_tertiary_light,
    onTertiary = md_on_tertiary_light,
    tertiaryContainer = md_tertiary_container_light,
    onTertiaryContainer = md_on_tertiary_container_light,
    error = md_error_light,
    onError = md_on_error_light,
    errorContainer = md_error_container_light,
    onErrorContainer = md_on_error_container_light,
    background = md_background_light,
    onBackground = md_on_background_light,
    surface = md_surface_light,
    onSurface = md_on_surface_light,
    surfaceVariant = md_surface_variant_light,
    onSurfaceVariant = md_on_surface_variant_light,
    surfaceTint = md_surface_tint_light,
    outline = md_outline_light,
    outlineVariant = md_outline_variant_light,
    inverseSurface = md_inverse_surface_light,
    inverseOnSurface = md_inverse_on_surface_light,
    inversePrimary = md_inverse_primary_light,
    scrim = md_scrim_light,
)

val DarkColorScheme = darkColorScheme(
    primary = md_primary_dark,
    onPrimary = md_on_primary_dark,
    primaryContainer = md_primary_container_dark,
    onPrimaryContainer = md_on_primary_container_dark,
    secondary = md_secondary_dark,
    onSecondary = md_on_secondary_dark,
    secondaryContainer = md_secondary_container_dark,
    onSecondaryContainer = md_on_secondary_container_dark,
    tertiary = md_tertiary_dark,
    onTertiary = md_on_tertiary_dark,
    tertiaryContainer = md_tertiary_container_dark,
    onTertiaryContainer = md_on_tertiary_container_dark,
    error = md_error_dark,
    onError = md_on_error_dark,
    errorContainer = md_error_container_dark,
    onErrorContainer = md_on_error_container_dark,
    background = md_background_dark,
    onBackground = md_on_background_dark,
    surface = md_surface_dark,
    onSurface = md_on_surface_dark,
    surfaceVariant = md_surface_variant_dark,
    onSurfaceVariant = md_on_surface_variant_dark,
    surfaceTint = md_surface_tint_dark,
    outline = md_outline_dark,
    outlineVariant = md_outline_variant_dark,
    inverseSurface = md_inverse_surface_dark,
    inverseOnSurface = md_inverse_on_surface_dark,
    inversePrimary = md_inverse_primary_dark,
    scrim = md_scrim_dark,
)

// ============================================
// MIUIX 配色方案 (浅色)
// ============================================

val MiuixLightColorScheme: ColorScheme
    get() = lightColorScheme(
        primary = miuix_primary,
        onPrimary = miuix_on_primary,
        primaryContainer = miuix_primary_container,
        onPrimaryContainer = miuix_on_primary_container,
        secondary = miuix_secondary,
        onSecondary = miuix_on_secondary,
        secondaryContainer = miuix_secondary_container,
        onSecondaryContainer = miuix_on_secondary_container,
        tertiary = miuix_tertiary_container,
        onTertiary = miuix_on_tertiary_container,
        tertiaryContainer = miuix_tertiary_container,
        onTertiaryContainer = miuix_on_tertiary_container,
        error = miuix_error,
        onError = miuix_on_error,
        errorContainer = miuix_error_container,
        onErrorContainer = miuix_on_error_container,
        background = miuix_background,
        onBackground = miuix_on_background,
        surface = miuix_surface,
        onSurface = miuix_on_surface,
        surfaceVariant = miuix_surface_variant,
        onSurfaceVariant = miuix_on_surface_secondary,
        surfaceTint = miuix_primary,
        outline = miuix_outline,
        outlineVariant = miuix_divider,
        inverseSurface = miuix_surface_dark_theme,
        inverseOnSurface = miuix_on_surface_dark_theme,
        inversePrimary = miuix_primary_dark_theme,
        scrim = miuix_window_dimming,
    )

// ============================================
// MIUIX 配色方案 (深色)
// ============================================

val MiuixDarkColorScheme: ColorScheme
    get() = darkColorScheme(
        primary = miuix_primary_dark_theme,
        onPrimary = miuix_on_primary_dark_theme,
        primaryContainer = miuix_primary_container_dark,
        onPrimaryContainer = miuix_on_primary_container_dark,
        secondary = miuix_secondary_dark_theme,
        onSecondary = miuix_on_secondary_dark_theme,
        secondaryContainer = miuix_secondary_container_dark,
        onSecondaryContainer = miuix_on_secondary_container_dark,
        tertiary = miuix_tertiary_container_dark,
        onTertiary = miuix_on_tertiary_container_dark,
        tertiaryContainer = miuix_tertiary_container_dark,
        onTertiaryContainer = miuix_on_tertiary_container_dark,
        error = miuix_error_dark_theme,
        onError = miuix_on_error_dark_theme,
        errorContainer = miuix_error_container_dark,
        onErrorContainer = miuix_on_error_container_dark,
        background = miuix_background_dark_theme,
        onBackground = miuix_on_background_dark_theme,
        surface = miuix_surface_dark_theme,
        onSurface = miuix_on_surface_dark_theme,
        surfaceVariant = miuix_surface_variant_dark,
        onSurfaceVariant = miuix_on_surface_secondary_dark,
        surfaceTint = miuix_primary_dark_theme,
        outline = miuix_outline_dark,
        outlineVariant = miuix_divider_dark,
        inverseSurface = miuix_surface,
        inverseOnSurface = miuix_on_surface,
        inversePrimary = miuix_primary,
        scrim = miuix_window_dimming_dark,
    )
