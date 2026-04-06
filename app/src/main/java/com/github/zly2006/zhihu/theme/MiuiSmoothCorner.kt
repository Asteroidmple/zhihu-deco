package com.github.zly2006.zhihu.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * MIUI 风格的平滑圆角形状
 */
class MiuiSmoothCornerShape(
    private val radius: Dp = 16.dp,
    private val smoothness: Float = 0.6f,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val radiusPx = with(density) { radius.toPx() }
        val path = createSuperellipsePath(size, radiusPx, smoothness)
        return Outline.Generic(path)
    }

    private fun createSuperellipsePath(
        size: Size,
        radius: Float,
        smoothness: Float,
    ): Path {
        val path = Path()
        val width = size.width
        val height = size.height
        val r = radius.coerceAtMost(width / 2).coerceAtMost(height / 2)
        val factor = 0.55f + (smoothness * 0.15f)

        path.moveTo(r, 0f)
        path.lineTo(width - r, 0f)
        path.cubicTo(
            width - r * (1 - factor),
            0f,
            width,
            r * (1 - factor),
            width,
            r,
        )
        path.lineTo(width, height - r)
        path.cubicTo(
            width,
            height - r * (1 - factor),
            width - r * (1 - factor),
            height,
            width - r,
            height,
        )
        path.lineTo(r, height)
        path.cubicTo(
            r * (1 - factor),
            height,
            0f,
            height - r * (1 - factor),
            0f,
            height - r,
        )
        path.lineTo(0f, r)
        path.cubicTo(
            0f,
            r * (1 - factor),
            r * (1 - factor),
            0f,
            r,
            0f,
        )
        path.close()
        return path
    }
}

object MiuiSmoothCorners {
    val Small = MiuiSmoothCornerShape(radius = 8.dp, smoothness = 0.5f)
    val Medium = MiuiSmoothCornerShape(radius = 12.dp, smoothness = 0.6f)
    val Large = MiuiSmoothCornerShape(radius = 20.dp, smoothness = 0.7f)
    val ExtraLarge = MiuiSmoothCornerShape(radius = 28.dp, smoothness = 0.75f)
}

@Composable
fun cardShape(): androidx.compose.ui.graphics.Shape =
    if (isMiuixTheme()) MiuiSmoothCorners.Medium else RoundedCornerShape(12.dp)

@Composable
fun buttonShape(): androidx.compose.ui.graphics.Shape =
    if (isMiuixTheme()) MiuiSmoothCorners.Small else RoundedCornerShape(8.dp)

@Composable
fun dialogShape(): androidx.compose.ui.graphics.Shape =
    if (isMiuixTheme()) MiuiSmoothCorners.Large else RoundedCornerShape(16.dp)

@Composable
fun bottomSheetShape(): androidx.compose.ui.graphics.Shape =
    if (isMiuixTheme()) {
        RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    } else {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    }
