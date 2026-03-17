package com.github.zly2006.zhihu.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// ============================================
// Material Design 3 形状系统
// ============================================

val Shapes = Shapes(
    // 小形状 - 按钮、芯片等
    small = RoundedCornerShape(8.dp),
    // 中形状 - 卡片、对话框等
    medium = RoundedCornerShape(12.dp),
    // 大形状 - 底部表单、导航抽屉等
    large = RoundedCornerShape(16.dp),
)

// ============================================
// MIUIX 风格形状
// ============================================

val MiuixShapes = Shapes(
    // MIUIX 使用更圆的角
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
)

// ============================================
// 组件特定形状
// ============================================

// 卡片形状
val CardShape = RoundedCornerShape(12.dp)
val MiuixCardShape = RoundedCornerShape(16.dp)

// 按钮形状
val ButtonShape = RoundedCornerShape(8.dp)
val MiuixButtonShape = RoundedCornerShape(10.dp)

// 输入框形状
val TextFieldShape = RoundedCornerShape(8.dp)
val MiuixTextFieldShape = RoundedCornerShape(12.dp)

// 对话框形状
val DialogShape = RoundedCornerShape(16.dp)
val MiuixDialogShape = RoundedCornerShape(24.dp)

// 底部表单形状
val BottomSheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
val MiuixBottomSheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)

// 导航栏形状
val NavigationBarShape = RoundedCornerShape(24.dp)
