/*
 * Zhihu++ - Free & Ad-Free Zhihu client for Android.
 * Copyright (C) 2024-2026, zly2006 <i@zly2006.me>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation (version 3 only).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.zly2006.zhihu.theme

<<<<<<< HEAD:app/src/main/java/com/github/zly2006/zhihu/theme/Theme.kt
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
=======
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.dynamicColorScheme
>>>>>>> afb58205039fb418bd264b83544cc9e612ab9299:shared/src/commonMain/kotlin/com/github/zly2006/zhihu/theme/Theme.kt

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
<<<<<<< HEAD:app/src/main/java/com/github/zly2006/zhihu/theme/Theme.kt
=======

@Composable
fun ZhihuTheme(
    content: @Composable () -> Unit,
) {
    val useDynamicColor = ThemeManager.getUseDynamicColor()
    val customBackgroundColor = ThemeManager.getBackgroundColor()
    val darkTheme = ThemeManager.isDarkTheme()
    val platformDynamicColorScheme = platformDynamicColorScheme(darkTheme)

    val baseColorScheme = when {
        useDynamicColor && platformDynamicColorScheme != null -> platformDynamicColorScheme
        !useDynamicColor -> {
            dynamicColorScheme(
                seedColor = ThemeManager.getCustomColor(),
                isDark = darkTheme,
                isAmoled = false,
            )
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val colorScheme = baseColorScheme.copy(
        background = customBackgroundColor,
        surface = customBackgroundColor,
    )

    PlatformSystemBarEffect(darkTheme)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}

@Composable
expect fun currentSystemInDarkTheme(): Boolean

@Composable
expect fun platformDynamicColorScheme(darkTheme: Boolean): ColorScheme?

@Composable
expect fun PlatformSystemBarEffect(darkTheme: Boolean)
>>>>>>> afb58205039fb418bd264b83544cc9e612ab9299:shared/src/commonMain/kotlin/com/github/zly2006/zhihu/theme/Theme.kt
