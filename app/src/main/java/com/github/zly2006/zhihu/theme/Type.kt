package com.github.zly2006.zhihu.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

// ============================================
// MIUIX 官方文本样式
// 基于 https://compose-miuix-ui.github.io/miuix/zh_CN/guide/textstyles
// ============================================

/**
 * MIUIX 文本样式数据类
 * 包含所有 MIUIX 标准文本样式
 */
data class MiuixTextStyles(
    // 主要样式
    val main: TextStyle,
    // 段落样式
    val paragraph: TextStyle,
    // 正文样式
    val body1: TextStyle,
    val body2: TextStyle,
    // 按钮样式
    val button: TextStyle,
    // 脚注样式
    val footnote1: TextStyle,
    val footnote2: TextStyle,
    // 标题样式
    val headline1: TextStyle,
    val headline2: TextStyle,
    // 副标题样式
    val subtitle: TextStyle,
    // 大标题样式
    val title1: TextStyle,
    val title2: TextStyle,
    val title3: TextStyle,
    val title4: TextStyle,
)

/**
 * 创建默认 MIUIX 文本样式
 * 所有样式的颜色会在运行时由 MiuixTheme.colorScheme.onBackground 统一设置
 */
fun defaultMiuixTextStyles(): MiuixTextStyles = MiuixTextStyles(
    // 主要样式 - 17sp, Normal
    main = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 1.2.em,
    ),
    // 段落样式 - 17sp, Normal
    paragraph = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 1.2.em,
    ),
    // 正文样式 1 - 16sp, Normal
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 1.2.em,
    ),
    // 正文样式 2 - 14sp, Normal
    body2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 1.2.em,
    ),
    // 按钮样式 - 17sp, Normal
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 1.2.em,
    ),
    // 脚注样式 1 - 13sp, Normal
    footnote1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 1.2.em,
    ),
    // 脚注样式 2 - 11sp, Normal
    footnote2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 1.2.em,
    ),
    // 标题样式 1 - 17sp, Normal
    headline1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 1.2.em,
    ),
    // 标题样式 2 - 16sp, Normal
    headline2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 1.2.em,
    ),
    // 副标题样式 - 14sp, Bold
    subtitle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 1.2.em,
    ),
    // 大标题样式 1 - 32sp, Normal
    title1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 1.2.em,
    ),
    // 大标题样式 2 - 24sp, Normal
    title2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 1.2.em,
    ),
    // 大标题样式 3 - 20sp, Normal
    title3 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 1.2.em,
    ),
    // 大标题样式 4 - 18sp, Normal
    title4 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 1.2.em,
    ),
)

// ============================================
// Material Design 3 文本样式（保留用于 Material 主题）
// ============================================

val Typography = Typography(
    // 标题样式
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W400,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.25).sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W400,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W400,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    // 标题样式
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    // 正文样式
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    // 标签样式
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)

// ============================================
// 全局 MIUIX 文本样式实例
// ============================================

val MiuixTypography = defaultMiuixTextStyles()
