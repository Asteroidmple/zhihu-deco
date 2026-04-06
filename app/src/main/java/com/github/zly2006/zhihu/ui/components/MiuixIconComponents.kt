package com.github.zly2006.zhihu.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.github.zly2006.zhihu.theme.isMiuixTheme

/**
 * MIUIX 图标组件
 * 根据当前主题自动适配 Material 或 MIUIX 图标
 */
@Composable
fun ZhihuIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = if (isMiuixTheme()) {
        // MIUIX 主题下使用 MIUIX 颜色
        androidx.compose.material3.MaterialTheme.colorScheme.onSurface
    } else {
        // Material 主题下使用 Material 颜色
        androidx.compose.material3.MaterialTheme.colorScheme.onSurface
    },
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint,
    )
}

/**
 * MIUIX 图标尺寸
 */
object MiuixIconSizes {
    /** 小图标 - 16dp */
    val Small = 16.dp

    /** 默认图标 - 24dp */
    val Default = 24.dp

    /** 中等图标 - 32dp */
    val Medium = 32.dp

    /** 大图标 - 48dp */
    val Large = 48.dp

    /** 超大图标 - 64dp */
    val ExtraLarge = 64.dp
}

/**
 * MIUIX 图标粗细
 */
enum class MiuixIconWeight {
    LIGHT, // 细线
    REGULAR, // 常规
    HEAVY, // 粗线
}

/**
 * 带尺寸的 MIUIX 图标
 */
@Composable
fun MiuixSizedIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = MiuixIconSizes.Default,
    tint: Color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier.size(size),
        tint = tint,
    )
}
