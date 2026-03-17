package com.github.zly2006.zhihu.theme

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

/**
 * MIUIX 风格动画规格
 * 提供流畅、自然的动画效果
 */
object MiuixAnimationSpec {
    // ============================================
    // 缓动函数
    // ============================================

    /** MIUIX 标准缓动 - 快速开始，缓慢结束 */
    val standardEasing: Easing = FastOutSlowInEasing

    /** MIUIX 强调缓动 - 更有弹性的效果 */
    val emphasizedEasing: Easing = androidx.compose.animation.core
        .CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)

    /** MIUIX 减速缓动 - 用于元素进入 */
    val decelerateEasing: Easing = androidx.compose.animation.core
        .CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)

    /** MIUIX 加速缓动 - 用于元素退出 */
    val accelerateEasing: Easing = androidx.compose.animation.core
        .CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f)

    // ============================================
    // 持续时间
    // ============================================

    /** 快速动画 - 150ms */
    const val DURATION_FAST = 150

    /** 正常动画 - 300ms */
    const val DURATION_NORMAL = 300

    /** 慢速动画 - 500ms */
    const val DURATION_SLOW = 500

    /** 强调动画 - 400ms */
    const val DURATION_EMPHASIZED = 400

    // ============================================
    // 标准动画规格
    // ============================================

    /** 快速淡入淡出 */
    val fadeFast = tween<Float>(
        durationMillis = DURATION_FAST,
        easing = standardEasing,
    )

    /** 正常淡入淡出 */
    val fadeNormal = tween<Float>(
        durationMillis = DURATION_NORMAL,
        easing = standardEasing,
    )

    /** 慢速淡入淡出 */
    val fadeSlow = tween<Float>(
        durationMillis = DURATION_SLOW,
        easing = standardEasing,
    )

    /** 快速位移动画 */
    val translateFast = tween<Float>(
        durationMillis = DURATION_FAST,
        easing = emphasizedEasing,
    )

    /** 正常位移动画 */
    val translateNormal = tween<Float>(
        durationMillis = DURATION_NORMAL,
        easing = emphasizedEasing,
    )

    /** 慢速位移动画 */
    val translateSlow = tween<Float>(
        durationMillis = DURATION_SLOW,
        easing = emphasizedEasing,
    )

    /** 缩放动画 */
    val scale = tween<Float>(
        durationMillis = DURATION_NORMAL,
        easing = emphasizedEasing,
    )

    // ============================================
    // 弹性动画
    // ============================================

    /** 轻弹动画 - 用于小元素 */
    val springLight = spring<Dp>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow,
    )

    /** 中等弹性动画 - 默认推荐 */
    val springMedium = spring<Dp>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium,
    )

    /** 强弹动画 - 用于需要强调的元素 */
    val springHigh = spring<Dp>(
        dampingRatio = Spring.DampingRatioHighBouncy,
        stiffness = Spring.StiffnessHigh,
    )

    /** 无弹性动画 - 用于精确控制 */
    val springNoBounce = spring<Dp>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium,
    )

    // Float 类型的弹性动画
    val springFloatLight = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow,
    )

    val springFloatMedium = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium,
    )

    // ============================================
    // 场景特定动画
    // ============================================

    /** 卡片点击效果 */
    val cardPress = tween<Float>(
        durationMillis = 100,
        easing = standardEasing,
    )

    /** 列表项滑动 */
    val listSwipe = tween<Float>(
        durationMillis = DURATION_NORMAL,
        easing = emphasizedEasing,
    )

    /** 页面切换 */
    val pageTransition = tween<Float>(
        durationMillis = DURATION_EMPHASIZED,
        easing = emphasizedEasing,
    )

    /** 底部表单展开 */
    val bottomSheetExpand = tween<Float>(
        durationMillis = DURATION_EMPHASIZED,
        easing = decelerateEasing,
    )

    /** 底部表单收起 */
    val bottomSheetCollapse = tween<Float>(
        durationMillis = DURATION_NORMAL,
        easing = accelerateEasing,
    )

    /** 对话框显示 */
    val dialogShow = tween<Float>(
        durationMillis = DURATION_NORMAL,
        easing = decelerateEasing,
    )

    /** 对话框消失 */
    val dialogDismiss = tween<Float>(
        durationMillis = DURATION_FAST,
        easing = accelerateEasing,
    )

    /** 导航栏切换 */
    val navigationSwitch = tween<Float>(
        durationMillis = DURATION_FAST,
        easing = standardEasing,
    )

    /** 按钮点击反馈 */
    val buttonPress = tween<Float>(
        durationMillis = 100,
        easing = LinearEasing,
    )

    /** 开关切换 */
    val switchToggle = tween<Float>(
        durationMillis = 200,
        easing = emphasizedEasing,
    )
}

/**
 * Material Design 动画规格
 * 用于 Material Design 主题下的标准动画
 */
object MaterialAnimationSpec {
    val fadeFast = tween<Float>(
        durationMillis = 150,
        easing = FastOutSlowInEasing,
    )

    val fadeNormal = tween<Float>(
        durationMillis = 300,
        easing = FastOutSlowInEasing,
    )

    val translateNormal = tween<Float>(
        durationMillis = 300,
        easing = FastOutSlowInEasing,
    )

    val scale = tween<Float>(
        durationMillis = 300,
        easing = FastOutSlowInEasing,
    )

    val springMedium = spring<Dp>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium,
    )
}

/**
 * 根据当前主题获取适当的动画规格
 */
@Composable
fun <T> themeAnimationSpec(
    material: T,
    miuix: T,
): T = if (isMiuixTheme()) miuix else material
