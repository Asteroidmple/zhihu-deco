package com.github.zly2006.zhihu.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 分层图标组件
 * 实现多层叠加的视觉效果
 */
@Composable
fun LayeredIcon(
    modifier: Modifier = Modifier,
    size: Dp = 256.dp,
    layers: List<IconLayer> = defaultIconLayers(),
) {
    Canvas(
        modifier = modifier.size(size),
    ) {
        val canvasSize = this.size
        val baseCornerRadius = canvasSize.width * 0.2f

        // 从底层到顶层绘制每个图层
        layers.forEachIndexed { index, layer ->
            val layerScale = 1.0f - (index * 0.1f)
            val layerSize = canvasSize.width * layerScale
            val layerPadding = (canvasSize.width - layerSize) / 2
            val layerCornerRadius = baseCornerRadius * layerScale

            drawLayer(
                layer = layer,
                offset = Offset(layerPadding, layerPadding),
                size = Size(layerSize, layerSize),
                cornerRadius = layerCornerRadius,
            )
        }
    }
}

/**
 * 图标图层数据类
 */
data class IconLayer(
    val color: Color,
    val gradientColors: List<Color> = emptyList(),
    val alpha: Float = 1.0f,
    val drawContent: (DrawScope, Offset, Size, Float) -> Unit = { _, _, _, _ -> },
    val hasGloss: Boolean = false,
)

/**
 * 默认图标图层
 */
fun defaultIconLayers(): List<IconLayer> = listOf(
    // 底层 - 阴影层
    IconLayer(
        color = Color(0xFF3482FF).copy(alpha = 0.3f),
        alpha = 0.5f,
    ),
    // 中层 - 渐变层
    IconLayer(
        color = Color(0xFF3482FF),
        gradientColors = listOf(
            Color(0xFF3482FF),
            Color(0xFF66B2FF),
        ),
    ),
    // 顶层 - 高光层
    IconLayer(
        color = Color.White,
        alpha = 0.15f,
        hasGloss = true,
    ),
)

/**
 * 绘制单个图层
 */
private fun DrawScope.drawLayer(
    layer: IconLayer,
    offset: Offset,
    size: Size,
    cornerRadius: Float,
) {
    // 绘制圆角矩形背景
    val roundRect = RoundRect(
        left = offset.x,
        top = offset.y,
        right = offset.x + size.width,
        bottom = offset.y + size.height,
        cornerRadius = CornerRadius(cornerRadius),
    )

    // 创建画笔路径
    val path = Path().apply {
        addRoundRect(roundRect)
    }

    // 绘制背景
    if (layer.gradientColors.isNotEmpty()) {
        drawPath(
            path = path,
            brush = Brush.linearGradient(
                colors = layer.gradientColors,
                start = offset,
                end = Offset(offset.x + size.width, offset.y + size.height),
            ),
            alpha = layer.alpha,
        )
    } else {
        drawPath(
            path = path,
            color = layer.color,
            alpha = layer.alpha,
        )
    }

    // 绘制高光效果
    if (layer.hasGloss) {
        drawGlossEffect(
            offset = offset,
            size = size,
            cornerRadius = cornerRadius,
        )
    }

    // 绘制自定义内容
    layer.drawContent(this, offset, size, cornerRadius)
}

/**
 * 绘制高光效果
 */
private fun DrawScope.drawGlossEffect(
    offset: Offset,
    size: Size,
    cornerRadius: Float,
) {
    val glossHeight = size.height * 0.4f
    val glossRect = RoundRect(
        left = offset.x,
        top = offset.y,
        right = offset.x + size.width,
        bottom = offset.y + glossHeight,
        topLeftCornerRadius = CornerRadius(cornerRadius),
        topRightCornerRadius = CornerRadius(cornerRadius),
    )

    drawPath(
        path = Path().apply {
            addRoundRect(glossRect)
        },
        brush = Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.3f),
                Color.White.copy(alpha = 0.1f),
                Color.Transparent,
            ),
            startY = offset.y,
            endY = offset.y + glossHeight,
        ),
    )
}

/**
 * 创建知乎风格分层图标
 */
@Composable
fun ZhihuLayeredIcon(
    modifier: Modifier = Modifier,
    size: Dp = 256.dp,
) {
    LayeredIcon(
        modifier = modifier.size(size),
        layers = listOf(
            // 底层
            IconLayer(
                color = Color(0xFF0066CC).copy(alpha = 0.3f),
                alpha = 0.6f,
            ),
            // 中层 - 主色渐变
            IconLayer(
                color = Color(0xFF0066CC),
                gradientColors = listOf(
                    Color(0xFF0066CC),
                    Color(0xFF3482FF),
                ),
                drawContent = { scope, offset, iconSize, cornerRadius ->
                    // 绘制"知"字简化图标
                    scope.drawZhihuCharacter(
                        color = Color.White,
                        offset = offset,
                        size = iconSize,
                        cornerRadius = cornerRadius,
                    )
                },
            ),
            // 顶层 - 高光
            IconLayer(
                color = Color.White,
                alpha = 0.12f,
                hasGloss = true,
            ),
        ),
    )
}

/**
 * 绘制"知"字简化图标
 */
private fun DrawScope.drawZhihuCharacter(
    color: Color,
    offset: Offset,
    size: Size,
    cornerRadius: Float,
) {
    val padding = size.width * 0.2f
    val iconSize = size.width - padding * 2
    val centerX = offset.x + size.width / 2
    val centerY = offset.y + size.height / 2

    // 左侧"矢"部（简化为圆形）
    val leftCircleRadius = iconSize * 0.15f
    drawCircle(
        color = color,
        radius = leftCircleRadius,
        center = Offset(
            centerX - iconSize * 0.2f,
            centerY,
        ),
    )

    // 右侧"口"部（简化为矩形）
    val rightRectWidth = iconSize * 0.25f
    val rightRectHeight = iconSize * 0.3f
    drawRoundRect(
        color = color,
        topLeft = Offset(
            centerX + iconSize * 0.1f,
            centerY - rightRectHeight / 2,
        ),
        size = androidx.compose.ui.geometry
            .Size(rightRectWidth, rightRectHeight),
        cornerRadius = CornerRadius(rightRectWidth * 0.2f),
    )
}

/**
 * 创建应用图标图层
 */
fun createAppIconLayers(
    baseColor: Color,
    showLogo: Boolean = true,
): List<IconLayer> = listOf(
    // 阴影层
    IconLayer(
        color = baseColor.copy(alpha = 0.2f),
        alpha = 0.5f,
    ),
    // 渐变层
    IconLayer(
        color = baseColor,
        gradientColors = listOf(
            baseColor,
            baseColor.copy(alpha = 0.7f),
        ),
        drawContent = if (showLogo) {
            { scope, offset, size, cornerRadius ->
                scope.drawAppLogo(
                    color = Color.White,
                    offset = offset,
                    size = size,
                )
            }
        } else {
            { _, _, _, _ -> }
        },
    ),
    // 高光层
    IconLayer(
        color = Color.White,
        alpha = 0.15f,
        hasGloss = true,
    ),
)

/**
 * 绘制应用 Logo
 */
private fun DrawScope.drawAppLogo(
    color: Color,
    offset: Offset,
    size: Size,
) {
    val centerX = offset.x + size.width / 2
    val centerY = offset.y + size.height / 2
    val logoSize = size.width * 0.5f

    // 简化的应用图标
    drawCircle(
        color = color,
        radius = logoSize * 0.3f,
        center = Offset(centerX, centerY),
    )
}
