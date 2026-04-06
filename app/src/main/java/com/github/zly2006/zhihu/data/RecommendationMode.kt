package com.github.zly2006.zhihu.data

enum class RecommendationMode(
    val displayName: String,
    val key: String,
    val description: String,
    val isExperimental: Boolean = false,
) {
    WEB("Web 端推荐", "server", "使用知乎网页端的推荐算法"),
    ANDROID("安卓端推荐", "android", "使用知乎安卓端的推荐算法"),
    LOCAL("本地推荐", "local", "基于本地数据的推荐算法（实验性功能）", isExperimental = true),
    MIXED("混合推荐", "mixed", "融合安卓和网页端推荐算法，并过滤严选内容", isExperimental = true),
}
