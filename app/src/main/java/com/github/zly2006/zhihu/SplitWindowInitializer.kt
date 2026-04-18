package com.github.zly2006.zhihu

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import androidx.startup.Initializer
import androidx.window.embedding.RuleController
import androidx.window.embedding.SplitController

/**
 * 平行视界分屏初始化器
 * 使用Jetpack Startup库在应用启动时加载分屏配置
 * 适配华为/vivo/oppo/荣耀/三星/小米等主流折叠屏设备
 */
class SplitWindowInitializer : Initializer<RuleController> {
    override fun create(
        @NonNull context: Context,
    ): RuleController {
        val splitSupportStatus = SplitController.getInstance(context).splitSupportStatus
        Log.d(TAG, "Activity Embedding support status: $splitSupportStatus")

        val ruleController = RuleController.getInstance(context)

        try {
            // 动态获取资源ID，避免R类引用问题
            val resId = context.resources.getIdentifier("main_split_config", "xml", context.packageName)
            if (resId != 0) {
                val rules = RuleController.parseRules(context, resId)
                ruleController.setRules(rules)
                Log.i(TAG, "Split rules loaded successfully")
            } else {
                Log.w(TAG, "main_split_config.xml not found")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load split rules", e)
        }

        return ruleController
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        // 无依赖，尽早初始化
        return emptyList()
    }

    companion object {
        private const val TAG = "SplitWindowInitializer"
    }
}
