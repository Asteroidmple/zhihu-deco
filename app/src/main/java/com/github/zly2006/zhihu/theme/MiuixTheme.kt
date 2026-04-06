package com.github.zly2006.zhihu.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.materialkolor.dynamicColorScheme
import top.yukonga.miuix.kmp.theme.MiuixTheme as MiuixComposeTheme

// ============================================
// 本地组合键
// ============================================

val LocalThemeStyle = compositionLocalOf { ThemeStyle.MATERIAL }
val LocalUseDynamicColor = compositionLocalOf { true }
val LocalUseMiuix = compositionLocalOf { false }

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
        LocalUseMiuix provides useMiuix,
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
fun <T> themeValue(material: T, miuix: T): T = if (isMiuixTheme()) miuix else material

@Composable
fun themeColor(material: Color, miuix: Color): Color = if (isMiuixTheme()) miuix else material
