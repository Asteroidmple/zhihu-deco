package com.github.zly2006.zhihu.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.materialkolor.dynamicColorScheme

// ============================================
// Material Design 3 浅色配色方案
// ============================================
val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    errorContainer = md_theme_light_errorContainer,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    surfaceTint = md_theme_light_surfaceTint,
    outline = md_theme_light_outline,
    outlineVariant = md_theme_light_outlineVariant,
    inverseSurface = md_theme_light_inverseSurface,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inversePrimary = md_theme_light_inversePrimary,
    scrim = md_theme_light_scrim,
)

// ============================================
// Material Design 3 深色配色方案
// ============================================
val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    errorContainer = md_theme_dark_errorContainer,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    surfaceTint = md_theme_dark_surfaceTint,
    outline = md_theme_dark_outline,
    outlineVariant = md_theme_dark_outlineVariant,
    inverseSurface = md_theme_dark_inverseSurface,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    scrim = md_theme_dark_scrim,
)

// ============================================
// MIUIX 官方浅色配色方案
// 基于 https://compose-miuix-ui.github.io/miuix/zh_CN/guide/colors
// ============================================
val MiuixLightColorScheme = lightColorScheme(
    // 主色系统
    primary = miuix_primary_light,
    onPrimary = miuix_onPrimary_light,
    primaryContainer = miuix_primaryContainer_light,
    onPrimaryContainer = miuix_onPrimaryContainer_light,
    // 次要色系统
    secondary = miuix_secondary_light,
    onSecondary = miuix_onSecondary_light,
    secondaryContainer = miuix_secondaryContainer_light,
    onSecondaryContainer = miuix_onSecondaryContainer_light,
    // 错误色系统
    error = miuix_error_light,
    onError = miuix_onError_light,
    errorContainer = miuix_errorContainer_light,
    onErrorContainer = miuix_onErrorContainer_light,
    // 背景和表面
    background = miuix_background_light,
    onBackground = miuix_onBackground_light,
    surface = miuix_surface_light,
    onSurface = miuix_onSurface_light,
    surfaceVariant = miuix_surfaceVariant_light,
    onSurfaceVariant = miuix_onSurfaceVariantSummary_light,
    // 轮廓
    outline = miuix_outline_light,
    outlineVariant = miuix_dividerLine_light,
    // 反色
    inverseSurface = miuix_surface_dark,
    inverseOnSurface = miuix_onSurface_dark,
    inversePrimary = miuix_primary_dark,
    // 其他
    scrim = Color(0xFF000000),
    surfaceTint = miuix_primary_light,
)

// ============================================
// MIUIX 官方深色配色方案
// 基于 https://compose-miuix-ui.github.io/miuix/zh_CN/guide/colors
// ============================================
val MiuixDarkColorScheme = darkColorScheme(
    // 主色系统
    primary = miuix_primary_dark,
    onPrimary = miuix_onPrimary_dark,
    primaryContainer = miuix_primaryContainer_dark,
    onPrimaryContainer = miuix_onPrimaryContainer_dark,
    // 次要色系统
    secondary = miuix_secondary_dark,
    onSecondary = miuix_onSecondary_dark,
    secondaryContainer = miuix_secondaryContainer_dark,
    onSecondaryContainer = miuix_onSecondaryContainer_dark,
    // 错误色系统
    error = miuix_error_dark,
    onError = miuix_onError_dark,
    errorContainer = miuix_errorContainer_dark,
    onErrorContainer = miuix_onErrorContainer_dark,
    // 背景和表面
    background = miuix_background_dark,
    onBackground = miuix_onBackground_dark,
    surface = miuix_surface_dark,
    onSurface = miuix_onSurface_dark,
    surfaceVariant = miuix_surfaceVariant_dark,
    onSurfaceVariant = miuix_onSurfaceVariantSummary_dark,
    // 轮廓
    outline = miuix_outline_dark,
    outlineVariant = miuix_dividerLine_dark,
    // 反色
    inverseSurface = miuix_surface_light,
    inverseOnSurface = miuix_onSurface_light,
    inversePrimary = miuix_primary_light,
    // 其他
    scrim = Color(0xFF000000),
    surfaceTint = miuix_primary_dark,
)

// ============================================
// 动态获取配色方案
// ============================================
fun getColorScheme(
    useMiuix: Boolean,
    darkTheme: Boolean,
    useDynamicColor: Boolean,
    customColor: Color? = null,
): ColorScheme = when {
    // MIUIX 主题
    useMiuix && darkTheme -> MiuixDarkColorScheme
    useMiuix && !darkTheme -> MiuixLightColorScheme
    // Material Design 动态取色
    useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        // 动态取色需要在 Composable 中使用 LocalContext
        // 这里返回基础配色方案
        if (darkTheme) DarkColorScheme else LightColorScheme
    }
    // Material Design 自定义颜色
    customColor != null -> dynamicColorScheme(
        seedColor = customColor,
        isDark = darkTheme,
        isAmoled = false,
    )
    // Material Design 默认
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
}
