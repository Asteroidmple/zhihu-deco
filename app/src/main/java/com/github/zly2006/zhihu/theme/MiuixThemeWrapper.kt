package com.github.zly2006.zhihu.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import top.yukonga.miuix.kmp.theme.ThemeController

/**
 * MIUIX 主题包装器
 * 将现有的 MD3 主题系统与 MIUIX 主题无缝集成
 * 
 * 特性:
 * - 支持深色/浅色模式切换
 * - 保留现有的 ThemeManager 配置
 * - 提供 MIUIX 特有的组件样式
 */
@Composable
fun ZhihuMiuixTheme(
    content: @Composable () -> Unit,
) {
    val darkTheme = ThemeManager.isDarkTheme()
    val themeController = remember {
        ThemeController(if (darkTheme) ColorSchemeMode.Dark else ColorSchemeMode.Light)
    }
    
    MiuixTheme(
        controller = themeController,
        content = content,
    )
}

/**
 * MIUIX 基础主题（不使用 ThemeController）
 * 适用于需要手动控制颜色方案的场景
 */
@Composable
fun ZhihuMiuixThemeSimple(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val themeController = remember {
        ThemeController(if (darkTheme) ColorSchemeMode.Dark else ColorSchemeMode.Light)
    }
    
    MiuixTheme(
        controller = themeController,
        content = content,
    )
}
