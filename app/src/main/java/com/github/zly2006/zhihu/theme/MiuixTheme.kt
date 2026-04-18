package com.github.zly2006.zhihu.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.github.zly2006.zhihu.util.configureSystemBars
import com.materialkolor.dynamicColorScheme
import androidx.compose.runtime.remember
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import top.yukonga.miuix.kmp.theme.ThemeController

// ============================================
// 本地组合键
// ============================================

val LocalUseDynamicColor = compositionLocalOf { true }

// ============================================
// Material Design 3 主题
// ============================================

@Composable
fun ZhihuTheme(
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val view = LocalView.current

    // 获取主题配置
    val useDynamicColor = ThemeManager.getUseDynamicColor()
    val customColor = ThemeManager.getCustomColor()
    val customBackgroundColor = ThemeManager.getBackgroundColor()
    val darkTheme = ThemeManager.isDarkTheme()

    // 计算配色方案
    val colorScheme = when {
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

    // 设置系统栏样式 - Edge-to-Edge 完整支持
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                // 配置系统栏透明化和图标颜色
                configureSystemBars(
                    window = it,
                    darkIcons = !darkTheme,
                    statusBarColor = Color.Transparent,
                    navigationBarColor = Color.Transparent,
                )
                
                // 使用 WindowCompat 控制外观
                WindowCompat.getInsetsController(it, view).apply {
                    isAppearanceLightStatusBars = !darkTheme
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }
        }
    }

    // 应用 MIUIX ThemeController + Material Design 3 主题
    // 使用 ThemeController 管理主题模式（System, Light, Dark, MonetSystem, MonetLight, MonetDark）
    val themeController = remember {
        ThemeController(
            when {
                useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> ColorSchemeMode.MonetSystem
                darkTheme -> ColorSchemeMode.Dark
                else -> ColorSchemeMode.Light
            },
        )
    }
    
    MiuixTheme(
        controller = themeController,
        content = {
            MaterialTheme(
                colorScheme = colorScheme,
                typography = Typography,
                shapes = Shapes,
                content = content,
            )
        },
    )
}

// ============================================
// 辅助函数
// ============================================

@Composable
fun isMaterialTheme(): Boolean = true
