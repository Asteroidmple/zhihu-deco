package com.github.zly2006.zhihu.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.github.zly2006.zhihu.theme.isMiuixTheme

/**
 * MIUIX 按压反馈效果
 * 提供视觉反馈来增强用户交互体验
 *
 * @param onClick 点击回调
 * @param modifier 修饰符
 * @param enabled 是否启用
 * @param pressScale 按压时的缩放比例
 * @param content 内容
 */
@Composable
fun MiuixPressable(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    pressScale: Float = 0.95f,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // 按压时的缩放动画
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) pressScale else 1.0f,
        label = "press_scale",
    )

    Box(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            ).scale(scale),
    ) {
        content()
    }
}

/**
 * 带按压反馈的卡片
 */
@Composable
fun MiuixPressableCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    if (isMiuixTheme()) {
        // MIUIX 主题下使用按压效果
        MiuixPressable(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            pressScale = 0.97f,
        ) {
            content()
        }
    } else {
        // Material 主题下使用标准 clickable
        Box(
            modifier = modifier.clickable(enabled = enabled, onClick = onClick),
        ) {
            content()
        }
    }
}

/**
 * 带按压反馈的按钮
 */
@Composable
fun MiuixPressableButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    MiuixPressable(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        pressScale = 0.92f,
    ) {
        content()
    }
}
