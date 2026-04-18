package com.github.zly2006.zhihu.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTopAppBar
import androidx.compose.material3.Text as M3Text

/**
 * MIUIX 组件示例
 * 展示如何在项目中使用 MIUIX 组件
 * 
 * MIUIX 提供的核心组件:
 * - BasicComponent: 基础卡片组件，类似 MD3 Card
 * - BlurBackground: 模糊背景效果
 * - Scaffold: MIUIX 风格的 Scaffold
 * - TopBar: MIUIX 风格的顶部导航栏
 * - Button/IconButton: MIUIX 风格的按钮
 * - Switch/Checkbox/Radio: MIUIX 风格的表单控件
 * - ListPreference: 设置列表项
 */
@Composable
fun MiuixComponentShowcase() {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = "MIUIX 组件示例",
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
            ) {
                // BasicComponent 示例 - 简单卡片
                BasicComponent(
                    title = "MIUIX 基础卡片",
                    summary = "这是 MIUIX 的基础卡片组件，类似 MD3 Card",
                    onClick = { },
                )
                
                // BasicComponent 示例 - 带开关
                BasicComponent(
                    title = "飞行模式",
                    summary = "点击开启或关闭",
                    onClick = { },
                )
                
                // 更多组件示例区域
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                ) {
                    M3Text(
                        text = "更多内容可以在此处添加",
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
    )
}
