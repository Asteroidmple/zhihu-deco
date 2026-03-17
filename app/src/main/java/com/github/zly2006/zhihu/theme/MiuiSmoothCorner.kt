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
 * 使用超椭圆 (superellipse) 曲线实现更自然的圆角效果
 *
 * @param radius 圆角半径
 * @param smoothness 平滑度 (0.0 - 1.0)，值越大越接近圆形
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

        // 限制圆角半径
        val r = radius.coerceAtMost(width / 2).coerceAtMost(height / 2)

        // 使用贝塞尔曲线近似超椭圆
        // 控制点因子，根据平滑度调整
        val factor = 0.55f + (smoothness * 0.15f)

        path.moveTo(r, 0f)

        // 上边
        path.lineTo(width - r, 0f)

        // 右上角
        path.cubicTo(
            width - r * (1 - factor),
            0f,
            width,
            r * (1 - factor),
            width,
            r,
        )

        // 右边
        path.lineTo(width, height - r)

        // 右下角
        path.cubicTo(
            width,
            height - r * (1 - factor),
            width - r * (1 - factor),
            height,
            width - r,
            height,
        )

        // 下边
        path.lineTo(r, height)

        // 左下角
        path.cubicTo(
            r * (1 - factor),
            height,
            0f,
            height - r * (1 - factor),
            0f,
            height - r,
        )

        // 左边
        path.lineTo(0f, r)

        // 左上角
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

/**
 * 创建不同大小的 MIUI 风格圆角形状
 */
object MiuiSmoothCorners {
    /** 小圆角 - 用于按钮、芯片 */
    val Small = MiuiSmoothCornerShape(radius = 8.dp, smoothness = 0.5f)

    /** 中等圆角 - 用于卡片 */
    val Medium = MiuiSmoothCornerShape(radius = 12.dp, smoothness = 0.6f)

    /** 大圆角 - 用于对话框 */
    val Large = MiuiSmoothCornerShape(radius = 20.dp, smoothness = 0.7f)

    /** 超大圆角 - 用于底部表单 */
    val ExtraLarge = MiuiSmoothCornerShape(radius = 28.dp, smoothness = 0.75f)

    /** 圆形 */
    val Circle = MiuiSmoothCornerShape(radius = 999.dp, smoothness = 1f)
}

/**
 * 根据当前主题获取适当的卡片形状
 */
@Composable
fun cardShape(): androidx.compose.ui.graphics.Shape = if (isMiuixTheme()) {
    MiuiSmoothCorners.Medium
} else {
    CardShape
}

/**
 * 根据当前主题获取适当的按钮形状
 */
@Composable
fun buttonShape(): androidx.compose.ui.graphics.Shape = if (isMiuixTheme()) {
    MiuiSmoothCorners.Small
} else {
    ButtonShape
}

/**
 * 根据当前主题获取适当的对话框形状
 */
@Composable
fun dialogShape(): androidx.compose.ui.graphics.Shape = if (isMiuixTheme()) {
    MiuiSmoothCorners.Large
} else {
    DialogShape
}

/**
 * 根据当前主题获取适当的底部表单形状
 */
@Composable
fun bottomSheetShape(): androidx.compose.ui.graphics.Shape = if (isMiuixTheme()) {
    androidx.compose.foundation.shape.RoundedCornerShape(
        topStart = 28.dp,
        topEnd = 28.dp,
    )
} else {
    BottomSheetShape
}
