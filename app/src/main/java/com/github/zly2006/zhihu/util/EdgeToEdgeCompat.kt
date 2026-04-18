package com.github.zly2006.zhihu.util

import android.os.Build
import android.view.View
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Edge-to-Edge 兼容工具类
 * 提供完整的系统栏透明化、Inset 处理和导航手势适配
 */

/**
 * 启用 Edge-to-Edge 模式
 * 根据当前主题自动配置系统栏样式
 */
inline fun ComponentActivity.enableEdgeToEdgeCompat() {
    enableEdgeToEdge()
    
    // Android 10+ 三按钮导航栏适配
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        window.isNavigationBarContrastEnforced = false
    }
    
    // Android 15+ Edge-to-Edge 强制模式
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
        window.setDecorFitsSystemWindows(false)
    }
}

/**
 * 配置系统栏样式
 * @param window 当前窗口
 * @param darkIcons 是否使用深色图标（浅色背景时为true）
 * @param statusBarColor 状态栏颜色（null为透明）
 * @param navigationBarColor 导航栏颜色（null为透明）
 */
fun configureSystemBars(
    window: Window,
    darkIcons: Boolean = true,
    statusBarColor: Color? = null,
    navigationBarColor: Color? = null,
) {
    // 状态栏配置
    window.statusBarColor = statusBarColor?.toArgb() ?: Color.Transparent.toArgb()
    
    // 导航栏配置
    window.navigationBarColor = navigationBarColor?.toArgb() ?: Color.Transparent.toArgb()
    
    // 导航栏对比度控制 (Android 10+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        window.isNavigationBarContrastEnforced = false
    }
    
    // 状态栏图标颜色控制
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val view = window.decorView
        var systemUiVisibility = view.systemUiVisibility
        systemUiVisibility = if (darkIcons) {
            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        view.systemUiVisibility = systemUiVisibility
    }
    
    // 导航栏图标颜色控制 (Android 26+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val view = window.decorView
        var systemUiVisibility = view.systemUiVisibility
        systemUiVisibility = if (darkIcons) {
            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        } else {
            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        }
        view.systemUiVisibility = systemUiVisibility
    }
}

/**
 * Compose 中获取系统栏 Insets 的辅助函数
 */
@Composable
fun rememberSystemBarInsets(): SystemBarInsets {
    val statusBarTopPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navigationBarBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val imeBottomPadding = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
    val displayCutoutPadding = WindowInsets.displayCutout.asPaddingValues()
    
    return SystemBarInsets(
        statusBarHeight = statusBarTopPadding,
        navigationBarHeight = navigationBarBottomPadding,
        imeHeight = imeBottomPadding,
        displayCutout = displayCutoutPadding,
    )
}

/**
 * 系统栏 Insets 数据类
 */
data class SystemBarInsets(
    val statusBarHeight: Dp = 0.dp,
    val navigationBarHeight: Dp = 0.dp,
    val imeHeight: Dp = 0.dp,
    val displayCutout: androidx.compose.foundation.layout.PaddingValues = androidx.compose.foundation.layout.PaddingValues(0.dp),
) {
    /**
     * 是否需要底部安全区域填充
     */
    val hasBottomInset: Boolean
        get() = navigationBarHeight > 0.dp || imeHeight > 0.dp
    
    /**
     * 是否需要顶部安全区域填充
     */
    val hasTopInset: Boolean
        get() = statusBarHeight > 0.dp
}

