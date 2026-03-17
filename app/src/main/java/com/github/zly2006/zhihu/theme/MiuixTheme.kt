package com.github.zly2006.zhihu.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.materialkolor.dynamicColorScheme
import top.yukonga.miuix.kmp.theme.MiuixTheme as MiuixComposeTheme

// ============================================
// 本地组合键
// ============================================

val LocalThemeStyle = compositionLocalOf { com.github.zly2006.zhihu.theme.ThemeStyle.MATERIAL }
val LocalUseDynamicColor = compositionLocalOf { true }
val LocalColorSchemeMode = compositionLocalOf { ColorSchemeMode.SYSTEM }

// ============================================
// 配色模式枚举
// ============================================

enum class ColorSchemeMode {
    SYSTEM, // 跟随系统
    LIGHT, // 强制浅色
    DARK, // 强制深色
    MONET_SYSTEM, // 莫奈取色，跟随系统
    MONET_LIGHT, // 莫奈取色，强制浅色
    MONET_DARK, // 莫奈取色，强制深色
}

// ============================================
// 调色板风格
// ============================================

enum class ThemePaletteStyle {
    TONAL_SPOT, // 默认 Material You 风格
    NEUTRAL, // 柔和低彩度
    VIBRANT, // 高彩度鲜艳
    EXPRESSIVE, // 大胆艺术感
    RAINBOW, // 广谱多色相
    FRUIT_SALAD, // 活泼多色相
    MONOCHROME, // 单色灰度
    FIDELITY, // 紧密匹配种子颜色
    CONTENT, // 从内容颜色派生
}

// ============================================
// 颜色规范版本
// ============================================

enum class ThemeColorSpec {
    SPEC_2021, // 原始 Material Design 3
    SPEC_2025, // 2025 更新版（仅部分调色板支持）
}

// ============================================
// ThemeController - 主题控制器
// ============================================

class ThemeController(
    initialMode: ColorSchemeMode = ColorSchemeMode.SYSTEM,
    val keyColor: Color? = null,
    val paletteStyle: ThemePaletteStyle = ThemePaletteStyle.TONAL_SPOT,
    val colorSpec: ThemeColorSpec = ThemeColorSpec.SPEC_2021,
    private val isDarkOverride: Boolean? = null,
) {
    var colorSchemeMode by mutableStateOf(initialMode)

    val isDark: Boolean
        @Composable
        get() = isDarkOverride ?: when (colorSchemeMode) {
            ColorSchemeMode.LIGHT, ColorSchemeMode.MONET_LIGHT -> false
            ColorSchemeMode.DARK, ColorSchemeMode.MONET_DARK -> true
            ColorSchemeMode.SYSTEM, ColorSchemeMode.MONET_SYSTEM -> isSystemInDarkTheme()
        }

    val isMonet: Boolean
        get() = when (colorSchemeMode) {
            ColorSchemeMode.MONET_SYSTEM, ColorSchemeMode.MONET_LIGHT, ColorSchemeMode.MONET_DARK -> true
            else -> false
        }

    fun setMode(mode: ColorSchemeMode) {
        colorSchemeMode = mode
    }
}

// ============================================
// 知乎主题包装器
// ============================================

@Composable
fun ZhihuTheme(
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val view = LocalView.current

    // 获取主题配置
    val useMiuix = ThemeManager.getUseMiuix()
    val useDynamicColor = ThemeManager.getUseDynamicColor()
    val customColor = ThemeManager.getCustomColor()
    val customBackgroundColor = ThemeManager.getBackgroundColor()
    val darkTheme = ThemeManager.isDarkTheme()
    val themeStyle = if (useMiuix) com.github.zly2006.zhihu.theme.ThemeStyle.MIUIX else com.github.zly2006.zhihu.theme.ThemeStyle.MATERIAL

    // 创建 ThemeController
    val controller = remember {
        val mode = when {
            useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
                if (darkTheme) ColorSchemeMode.MONET_DARK else ColorSchemeMode.MONET_LIGHT
            useDynamicColor ->
                ColorSchemeMode.SYSTEM
            else ->
                if (darkTheme) ColorSchemeMode.DARK else ColorSchemeMode.LIGHT
        }
        ThemeController(
            initialMode = mode,
            keyColor = if (!useDynamicColor) customColor else null,
            paletteStyle = ThemeManager.getPaletteStyleSync(),
            colorSpec = ThemeManager.getColorSpecSync(),
        )
    }

    // 计算配色方案
    val colorScheme = when {
        // MIUIX 主题
        useMiuix && darkTheme -> MiuixDarkColorScheme
        useMiuix && !darkTheme -> MiuixLightColorScheme
        // Material Design 动态取色 (Android 12+)
        useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }
        // Material Design 自定义颜色
        !useDynamicColor -> {
            dynamicColorScheme(
                seedColor = customColor,
                isDark = darkTheme,
                isAmoled = false,
            )
        }
        // Material Design 默认
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }.let { scheme ->
        // 应用自定义背景色
        if (customBackgroundColor != Color.Unspecified) {
            scheme.copy(
                background = customBackgroundColor,
                surface = customBackgroundColor,
            )
        } else {
            scheme
        }
    }

    // 设置系统栏样式
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                WindowCompat.getInsetsController(it, view).apply {
                    isAppearanceLightStatusBars = !darkTheme
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }
        }
    }

    // 提供主题上下文
    CompositionLocalProvider(
        LocalThemeStyle provides themeStyle,
        LocalUseDynamicColor provides useDynamicColor,
        LocalColorSchemeMode provides controller.colorSchemeMode,
    ) {
        if (useMiuix) {
            // MIUIX 主题 - 使用 MiuixComposeTheme
            MiuixComposeTheme(
                content = content,
            )
        } else {
            // Material Design 3 主题
            MaterialTheme(
                colorScheme = colorScheme,
                typography = Typography,
                shapes = Shapes,
                content = content,
            )
        }
    }
}

// ============================================
// 辅助函数
// ============================================

@Composable
fun currentThemeStyle(): com.github.zly2006.zhihu.theme.ThemeStyle = LocalThemeStyle.current

@Composable
fun isMiuixTheme(): Boolean = currentThemeStyle() == com.github.zly2006.zhihu.theme.ThemeStyle.MIUIX

@Composable
fun isMaterialTheme(): Boolean = currentThemeStyle() == com.github.zly2006.zhihu.theme.ThemeStyle.MATERIAL

@Composable
fun currentColorSchemeMode(): ColorSchemeMode = LocalColorSchemeMode.current

@Composable
fun isMonetEnabled(): Boolean = when (currentColorSchemeMode()) {
    ColorSchemeMode.MONET_SYSTEM, ColorSchemeMode.MONET_LIGHT, ColorSchemeMode.MONET_DARK -> true
    else -> false
}

@Composable
fun <T> themeValue(material: T, miuix: T): T = if (isMiuixTheme()) miuix else material

@Composable
fun themeColor(material: Color, miuix: Color): Color = if (isMiuixTheme()) miuix else material
