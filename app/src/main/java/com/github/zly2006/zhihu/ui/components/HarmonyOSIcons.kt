package com.github.zly2006.zhihu.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * HarmonyOS 风格分层图标
 * 基于 HarmonyOS App Icons 设计规范
 */
object HarmonyOSIcons {
    /**
     * 绘制分层背景
     * @param size 图标尺寸
     * @param baseColor 基础颜色
     * @param gradientColors 渐变颜色列表
     */
    @Composable
    fun LayeredBackground(
        modifier: Modifier = Modifier,
        size: Dp = 1024.dp,
        baseColor: Color = Color(0xFF3482FF),
        gradientColors: List<Color> = emptyList(),
    ) {
        Canvas(
            modifier = modifier.size(size),
        ) {
            val canvasSize = this.size
            val cornerRadius = canvasSize.width * 0.2f

            // 绘制底层阴影
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        baseColor.copy(alpha = 0.3f),
                        baseColor.copy(alpha = 0.1f),
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(canvasSize.width, canvasSize.height),
                ),
                topLeft = Offset(0f, 0f),
                size = canvasSize,
                cornerRadius = CornerRadius(cornerRadius),
            )

            // 绘制中层渐变
            if (gradientColors.isNotEmpty()) {
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = gradientColors,
                        start = Offset(0f, 0f),
                        end = Offset(canvasSize.width, canvasSize.height),
                    ),
                    topLeft = Offset(canvasSize.width * 0.05f, canvasSize.height * 0.05f),
                    size = Size(
                        canvasSize.width * 0.9f,
                        canvasSize.height * 0.9f,
                    ),
                    cornerRadius = CornerRadius(cornerRadius * 0.9f),
                )
            }

            // 绘制顶层高光
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.2f),
                        Color.Transparent,
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(canvasSize.width, canvasSize.height * 0.5f),
                ),
                topLeft = Offset(canvasSize.width * 0.1f, canvasSize.height * 0.1f),
                size = Size(
                    canvasSize.width * 0.8f,
                    canvasSize.height * 0.8f,
                ),
                cornerRadius = CornerRadius(cornerRadius * 0.8f),
            )
        }
    }

    /**
     * 绘制知乎图标
     */
    @Composable
    fun ZhihuIcon(
        modifier: Modifier = Modifier,
        size: Dp = 1024.dp,
    ) {
        Canvas(
            modifier = modifier.size(size),
        ) {
            val canvasSize = this.size
            val padding = canvasSize.width * 0.15f
            val iconSize = canvasSize.width - padding * 2

            // 绘制外层圆角矩形背景
            val outerCornerRadius = canvasSize.width * 0.2f
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0066CC),
                        Color(0xFF3482FF),
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(canvasSize.width, canvasSize.height),
                ),
                topLeft = Offset(0f, 0f),
                size = canvasSize,
                cornerRadius = CornerRadius(outerCornerRadius),
            )

            // 绘制中间层
            val innerCornerRadius = outerCornerRadius * 0.9f
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF3482FF),
                        Color(0xFF66B2FF),
                    ),
                    start = Offset(padding, padding),
                    end = Offset(canvasSize.width - padding, canvasSize.height - padding),
                ),
                topLeft = Offset(padding, padding),
                size = Size(iconSize, iconSize),
                cornerRadius = CornerRadius(innerCornerRadius),
            )

            // 绘制"知"字图标（简化版）
            drawZhihuLogo(
                color = Color.White,
                size = iconSize * 0.6f,
                offset = Offset(
                    padding + (iconSize - iconSize * 0.6f) / 2,
                    padding + (iconSize - iconSize * 0.6f) / 2,
                ),
            )

            // 绘制顶层高光
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.15f),
                        Color.Transparent,
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(canvasSize.width, canvasSize.height * 0.4f),
                ),
                topLeft = Offset(0f, 0f),
                size = canvasSize,
                cornerRadius = CornerRadius(outerCornerRadius),
            )
        }
    }

    /**
     * 绘制知乎 Logo（简化版）
     */
    private fun DrawScope.drawZhihuLogo(
        color: Color,
        size: Float,
        offset: Offset,
    ) {
        // 简化的"知"字图标 - 使用圆形和线条组合
        val centerX = offset.x + size / 2
        val centerY = offset.y + size / 2
        val radius = size * 0.15f

        // 左侧圆形
        drawCircle(
            color = color,
            radius = radius,
            center = Offset(centerX - radius * 1.5f, centerY),
        )

        // 右侧竖线
        drawLine(
            color = color,
            start = Offset(centerX + radius * 0.5f, centerY - radius),
            end = Offset(centerX + radius * 0.5f, centerY + radius),
            strokeWidth = radius * 0.4f,
        )

        // 右侧横线
        drawLine(
            color = color,
            start = Offset(centerX + radius * 0.5f, centerY),
            end = Offset(centerX + radius * 2f, centerY),
            strokeWidth = radius * 0.4f,
        )
    }

    /**
     * 创建渐变颜色方案
     */
    fun createGradientColors(
        baseColor: Color,
        variant: GradientVariant = GradientVariant.Default,
    ): List<Color> = when (variant) {
        GradientVariant.Default -> listOf(
            baseColor,
            baseColor.copy(alpha = 0.8f),
            baseColor.copy(alpha = 0.6f),
        )
        GradientVariant.Sunset -> listOf(
            Color(0xFFFF6B35),
            Color(0xFFF7C59F),
            Color(0xFFEFEFD0),
        )
        GradientVariant.Ocean -> listOf(
            Color(0xFF006994),
            Color(0xFF40E0D0),
            Color(0xFF4080FF),
        )
        GradientVariant.Forest -> listOf(
            Color(0xFF228B22),
            Color(0xFF32CD32),
            Color(0xFF90EE90),
        )
    }
}

enum class GradientVariant {
    Default,
    Sunset,
    Ocean,
    Forest,
}
