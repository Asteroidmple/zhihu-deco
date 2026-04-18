package com.github.zly2006.zhihu.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// ============================================
// Material Design 3 形状系统
// 基于 MD3 Token 规范定义
// ============================================

val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
)

// MD3 扩展形状 Token
object ShapeTokens {
    // Extra small - 用于小图标、徽章等
    val extraSmall = RoundedCornerShape(4.dp)
    
    // Small - 用于小按钮、输入框等
    val small = RoundedCornerShape(8.dp)
    
    // Medium - 用于卡片、对话框等
    val medium = RoundedCornerShape(12.dp)
    
    // Large - 用于大卡片、侧边栏等
    val large = RoundedCornerShape(16.dp)
    
    // Extra large - 用于全屏对话框、底部表格等
    val extraLarge = RoundedCornerShape(28.dp)
    
    // Full - 用于圆形元素
    val full = RoundedCornerShape(100)
}
