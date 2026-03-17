package com.github.zly2006.zhihu.viewmodel.local

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import com.github.zly2006.zhihu.data.CommonFeed
import com.github.zly2006.zhihu.data.Feed
import com.github.zly2006.zhihu.data.Person
import com.github.zly2006.zhihu.ui.IHomeFeedViewModel
import com.github.zly2006.zhihu.viewmodel.feed.BaseFeedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class LocalHomeFeedViewModel :
    BaseFeedViewModel(),
    IHomeFeedViewModel {
    private lateinit var recommendationEngine: LocalRecommendationEngine

    override val initialUrl: String
        get() = error("LocalHomeFeedViewModel should not be used directly. Use LocalFeedViewModel instead.")

    override suspend fun fetchFeeds(context: Context) {
        try {
            if (!::recommendationEngine.isInitialized) {
                recommendationEngine = LocalRecommendationEngine(context)
            }

            // 获取本地推荐内容
            val recommendations = recommendationEngine.generateRecommendations(20)

            // 转换为显示项目
            recommendations.forEach { localFeed ->
                val displayItem = createLocalFeedDisplayItem(localFeed)
                displayItems.add(displayItem)
            }
        } catch (e: Exception) {
            Log.e("LocalHomeFeedViewModel", "Error fetching local feeds", e)
            if (e.message?.contains("does not exist. Is Room annotation processor correctly configured?") == true) {
                withContext(Dispatchers.Main) {
                    AlertDialog
                        .Builder(context)
                        .setTitle("数据库错误")
                        .setMessage("本地推荐系统的数据库未正确初始化。请尝试重启应用或清除应用数据。")
                        .setPositiveButton("确定") { dialog, _ -> dialog.dismiss() }
                        .show()
                }
            }
            // 如果推荐引擎失败，提供备用内容
            generateFallbackContent()
        } finally {
            isLoading = false
        }
    }

    private fun createLocalFeedDisplayItem(localFeed: LocalFeed): FeedDisplayItem {
        // 根据 navDestination 创建对应的 Feed Target，以便标签系统能够识别
        val feed = localFeed.navDestination?.let { navDest ->
            when {
                // 判断是否是回答
                navDest.startsWith("answer/") -> {
                    val answerId = navDest.removePrefix("answer/").toLongOrNull()
                    if (answerId != null) {
                        CommonFeed(
                            id = localFeed.id,
                            verb = "LOCAL_RECOMMENDATION",
                            target = Feed.AnswerTarget(
                                id = answerId,
                                url = "https://www.zhihu.com/answer/$answerId",
                                author = null,
                                createdTime = localFeed.createdAt,
                                updatedTime = -1,
                                voteupCount = -1,
                                thanksCount = -1,
                                commentCount = -1,
                                isCopyable = false,
                                question = Feed.QuestionTarget(
                                    id = 0,
                                    _title = localFeed.title,
                                    url = "",
                                    type = "question",
                                    answerCount = 0,
                                    commentCount = 0,
                                    followerCount = 0,
                                    isFollowing = false,
                                ),
                                excerpt = localFeed.summary,
                            ),
                        )
                    } else {
                        null
                    }
                }
                // 判断是否是文章
                navDest.startsWith("article/") -> {
                    val articleId = navDest.removePrefix("article/").toLongOrNull()
                    if (articleId != null) {
                        CommonFeed(
                            id = localFeed.id,
                            verb = "LOCAL_RECOMMENDATION",
                            target = Feed.ArticleTarget(
                                id = articleId,
                                url = "https://zhuanlan.zhihu.com/p/$articleId",
                                author = Person(
                                    id = "",
                                    url = "",
                                    userType = "",
                                    urlToken = null,
                                    name = "",
                                    headline = "",
                                    avatarUrl = "",
                                    isOrg = false,
                                    gender = 0,
                                    followersCount = 0,
                                    isFollowing = false,
                                    isFollowed = false,
                                    badge = null,
                                ),
                                voteupCount = -1,
                                commentCount = -1,
                                title = localFeed.title,
                                excerpt = localFeed.summary,
                                content = "",
                                created = localFeed.createdAt,
                                updated = 0,
                                isLabeled = false,
                                visitedCount = 0,
                                favoriteCount = 0,
                            ),
                        )
                    } else {
                        null
                    }
                }
                // 判断是否是问题
                navDest.startsWith("question/") -> {
                    val questionId = navDest.removePrefix("question/").toLongOrNull()
                    if (questionId != null) {
                        CommonFeed(
                            id = localFeed.id,
                            verb = "LOCAL_RECOMMENDATION",
                            target = Feed.QuestionTarget(
                                id = questionId,
                                _title = localFeed.title,
                                url = "https://www.zhihu.com/question/$questionId",
                                type = "question",
                                answerCount = 0,
                                commentCount = 0,
                                followerCount = 0,
                                isFollowing = false,
                            ),
                        )
                    } else {
                        null
                    }
                }
                else -> null
            }
        }

        return FeedDisplayItem(
            title = localFeed.title,
            summary = localFeed.summary,
            details = localFeed.reasonDisplay,
            feed = feed,
            isFiltered = false,
        )
    }

    private suspend fun generateFallbackContent() {
        val fallbackItems = listOf(
            FeedDisplayItem(
                title = "本地推荐系统正在学习中...",
                summary = "随着您在应用中的使用，本地推荐系统会逐渐了解您的偏好，为您提供更精准的个性化内容。",
                details = "本地推荐 - 加载失败",
                feed = null,
                isFiltered = false,
            ),
            FeedDisplayItem(
                title = "隐私保护的个性化推荐",
                summary = "本地推荐模式完全在设备上运行，不会上传您的数据到服务器，确保您的隐私安全。",
                details = "本地推荐 - 加载失败",
                feed = null,
                isFiltered = false,
            ),
        )

        fallbackItems.forEach { item ->
            displayItems.add(item)
            delay(500)
        }
    }

    override suspend fun recordContentInteraction(
        context: Context,
        feed: Feed,
    ) {
    }

    override fun onUiContentClick(context: Context, feed: Feed, item: FeedDisplayItem) {
    }
}
