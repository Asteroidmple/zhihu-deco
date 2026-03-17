package com.github.zly2006.zhihu.viewmodel.za

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.github.zly2006.zhihu.Article
import com.github.zly2006.zhihu.ArticleType
import com.github.zly2006.zhihu.Pin
import com.github.zly2006.zhihu.Question
import com.github.zly2006.zhihu.Video
import com.github.zly2006.zhihu.data.AccountData
import com.github.zly2006.zhihu.data.CommonFeed
import com.github.zly2006.zhihu.data.Feed
import com.github.zly2006.zhihu.data.Person
import com.github.zly2006.zhihu.resolveContent
import com.github.zly2006.zhihu.ui.IHomeFeedViewModel
import com.github.zly2006.zhihu.ui.PREFERENCE_NAME
import com.github.zly2006.zhihu.viewmodel.feed.BaseFeedViewModel
import com.github.zly2006.zhihu.viewmodel.filter.ContentFilterExtensions
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.get
import io.ktor.http.decodeURLPart
import io.ktor.http.isSuccess
import io.ktor.util.appendAll
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import io.ktor.serialization.kotlinx.json.json as ktorJson

private val ZHIHU_PP_ANDROID_HEADERS = createClientPlugin("ZhihuPPAndroidHeaders", { }) {
    onRequest { request, _ ->
        request.headers.appendAll(AccountData.ANDROID_HEADERS)
    }
}

class AndroidHomeFeedViewModel :
    BaseFeedViewModel(),
    IHomeFeedViewModel {
    override val initialUrl: String
        get() = "https://api.zhihu.com/topstory/recommend"

    override fun httpClient(context: Context): HttpClient {
        // 检查是否启用推荐内容时登录设置
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val loginForRecommendation = preferences.getBoolean("loginForRecommendation", true)

        return HttpClient {
            install(ContentNegotiation) {
                ktorJson(AccountData.json)
            }
            install(UserAgent) {
                agent = AccountData.ANDROID_USER_AGENT
            }
            install(ZHIHU_PP_ANDROID_HEADERS)
            if (loginForRecommendation) {
                install(HttpCookies) {
                    storage = AccountData.cookieStorage(context, null)
                }
            }
        }
    }

    /**
     * Find the first JsonObject in the list where the value associated with [key] matches [value].
     */
    @Suppress("NOTHING_TO_INLINE")
    private inline fun List<JsonObject>.joStrMatch(key: String, value: String): JsonObject =
        this.firstOrNull { it[key]?.jsonPrimitive?.content == value }
            ?: throw IllegalStateException("No matching JsonObject found for $key = $value: $this")

    public override suspend fun fetchFeeds(context: Context) {
        try {
            val response = httpClient(context).get(lastPaging?.next ?: initialUrl)
            if (response.status.isSuccess()) {
                val jojo = response.body<JsonObject>()
                val data = jojo["data"]?.jsonArray ?: throw IllegalStateException("No data found in response")

                // 收集所有待显示的项目
                val itemsToDisplay = mutableListOf<FeedDisplayItem>()

                data
                    .map { it.jsonObject }
                    .forEach { card ->
                        try {
                            if (card["type"]?.jsonPrimitive?.content != "ComponentCard") {
                                return@forEach
                            }
                            val route =
                                card["action"]!!
                                    .jsonObject["parameter"]!!
                                    .jsonPrimitive.content
                                    .substringAfter("route_url=")
                            val routeDest = resolveContent(route.decodeURLPart().toUri()) ?: return@forEach
                            val children = card["children"]?.jsonArray?.map { it.jsonObject } ?: return@forEach
                            val title = children.joStrMatch("id", "Text")["text"]!!.jsonPrimitive.content
                            val summary = children.joStrMatch("id", "text_pin_summary")["text"]!!.jsonPrimitive.content
                            val footer = children.filter { it["type"]!!.jsonPrimitive.content == "Line" }.getOrNull(1) ?: return@forEach
                            val footerText = if (footer["style"]!!.jsonPrimitive.content == "LineFooterReaction_feed_v3") {
                                val footerLine = footer["elements"]!!.jsonArray.map { it.jsonObject }
                                val voteUp = footerLine.joStrMatch("reaction", "Vote")["count"]!!.jsonPrimitive.int
                                val comment = footerLine.joStrMatch("reaction", "Comment")["count"]!!.jsonPrimitive.int
                                val collect = footerLine.joStrMatch("reaction", "Collect")["count"]!!.jsonPrimitive.int
                                "$voteUp 赞同 · $comment 评论 · $collect 收藏"
                            } else {
                                val footerLine = footer["elements"]!!.jsonArray.map { it.jsonObject }
                                footerLine.joStrMatch("type", "Text")["text"]!!.jsonPrimitive.content
                            }
                            val lineAuthor =
                                children
                                    .first {
                                        it["style"]!!.jsonPrimitive.content.startsWith("RecommendAuthorLine") ||
                                            it["style"]!!.jsonPrimitive.content.startsWith("LineAuthor_default")
                                    }["elements"]!!
                                    .jsonArray
                                    .map { it.jsonObject }
                            val avatar = lineAuthor
                                .joStrMatch("style", "Avatar_default")["image"]!!
                                .jsonObject["url"]!!
                                .jsonPrimitive.content
                            val authorName = lineAuthor.joStrMatch("type", "Text")["text"]!!.jsonPrimitive.content
                            if (routeDest is Article) {
                                routeDest.authorName = authorName
                                routeDest.title = title
                                routeDest.avatarSrc = avatar
                            }

                            // 根据 NavDestination 创建对应的 Feed 对象，以便标签系统能够识别
                            val feed = createFeedFromDestination(routeDest, title, summary, authorName, avatar)

                            itemsToDisplay.add(
                                FeedDisplayItem(
                                    navDestination = routeDest,
                                    avatarSrc = avatar,
                                    authorName = authorName,
                                    summary = summary,
                                    title = title,
                                    details = "$footerText · 手机版推荐",
                                    feed = feed,
                                ),
                            )
                        } catch (e: Exception) {
                            Log.e("AndroidHomeFeedViewModel", "Failed to process card: $card", e)
                        }
                    }

                val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

                // 后台运行内容过滤
                val filteredItems = ContentFilterExtensions.applyContentFilterToDisplayItems(context, itemsToDisplay)
                val newDestinations = filteredItems.map { it.navDestination }.toSet()

                // 更新 displayItems：只添加未被过滤的条目
                withContext(Dispatchers.Main) {
                    // 移除之前加载但这次被过滤掉的条目
                    displayItems.removeAll { item ->
                        item.navDestination in newDestinations && filteredItems.none { it.navDestination == item.navDestination }
                    }

                    // 添加新的未被过滤的条目
                    filteredItems.forEach { item ->
                        if (displayItems.none { it.navDestination == item.navDestination }) {
                            displayItems.add(item)
                        }
                    }
                }

                lastPaging = if ("paging" in jojo) {
                    AccountData.decodeJson(jojo["paging"]!!)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            if (e !is CancellationException) {
                Log.e(this::class.simpleName, "Failed to fetch feeds", e)
                context.mainExecutor.execute {
                    Toast.makeText(context, "安卓端推荐加载失败: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            throw e
        } finally {
            isLoading = false
        }
    }

    override suspend fun recordContentInteraction(context: Context, feed: Feed) {
        // Android 版本暂不记录交互
    }

    override fun onUiContentClick(context: Context, feed: Feed, item: BaseFeedViewModel.FeedDisplayItem) {
        // 不发送已读状态到服务器
    }
}

/**
 * 根据 NavDestination 创建对应的 Feed 对象，以便标签系统能够识别
 */
private fun createFeedFromDestination(
    routeDest: com.github.zly2006.zhihu.NavDestination?,
    title: String,
    summary: String,
    authorName: String,
    avatar: String,
): Feed? = when (routeDest) {
    is Article -> {
        when (routeDest.type) {
            ArticleType.Answer -> {
                CommonFeed(
                    id = "android_${routeDest.id}",
                    verb = "ANDROID_RECOMMENDATION",
                    target = Feed.AnswerTarget(
                        id = routeDest.id,
                        url = "https://www.zhihu.com/answer/${routeDest.id}",
                        author = Person(
                            id = "",
                            url = "",
                            userType = "",
                            urlToken = null,
                            name = authorName,
                            headline = "",
                            avatarUrl = avatar,
                            isOrg = false,
                            gender = 0,
                            followersCount = 0,
                            isFollowing = false,
                            isFollowed = false,
                            badge = null,
                        ),
                        createdTime = -1,
                        updatedTime = -1,
                        voteupCount = -1,
                        thanksCount = -1,
                        commentCount = -1,
                        isCopyable = false,
                        question = Feed.QuestionTarget(
                            id = 0,
                            _title = title,
                            url = "",
                            type = "question",
                            answerCount = 0,
                            commentCount = 0,
                            followerCount = 0,
                            isFollowing = false,
                        ),
                        excerpt = summary,
                    ),
                )
            }
            ArticleType.Article -> {
                CommonFeed(
                    id = "android_${routeDest.id}",
                    verb = "ANDROID_RECOMMENDATION",
                    target = Feed.ArticleTarget(
                        id = routeDest.id,
                        url = "https://zhuanlan.zhihu.com/p/${routeDest.id}",
                        author = Person(
                            id = "",
                            url = "",
                            userType = "",
                            urlToken = null,
                            name = authorName,
                            headline = "",
                            avatarUrl = avatar,
                            isOrg = false,
                            gender = 0,
                            followersCount = 0,
                            isFollowing = false,
                            isFollowed = false,
                            badge = null,
                        ),
                        voteupCount = -1,
                        commentCount = -1,
                        title = title,
                        excerpt = summary,
                        content = "",
                        created = -1,
                        updated = 0,
                        isLabeled = false,
                        visitedCount = 0,
                        favoriteCount = 0,
                    ),
                )
            }
        }
    }
    is Question -> {
        CommonFeed(
            id = "android_question_${routeDest.questionId}",
            verb = "ANDROID_RECOMMENDATION",
            target = Feed.QuestionTarget(
                id = routeDest.questionId,
                _title = title,
                url = "https://www.zhihu.com/question/${routeDest.questionId}",
                type = "question",
                answerCount = 0,
                commentCount = 0,
                followerCount = 0,
                isFollowing = false,
            ),
        )
    }
    is Pin -> {
        CommonFeed(
            id = "android_pin_${routeDest.id}",
            verb = "ANDROID_RECOMMENDATION",
            target = Feed.PinTarget(
                id = routeDest.id,
                url = "https://www.zhihu.com/pin/${routeDest.id}",
                author = Person(
                    id = "",
                    url = "",
                    userType = "",
                    urlToken = null,
                    name = authorName,
                    headline = "",
                    avatarUrl = avatar,
                    isOrg = false,
                    gender = 0,
                    followersCount = 0,
                    isFollowing = false,
                    isFollowed = false,
                    badge = null,
                ),
                commentCount = 0,
                content = kotlinx.serialization.json.JsonArray(emptyList()),
                likeCount = 0,
                excerptTitle = title,
                contentHtml = summary,
                created = -1,
                updated = 0,
                reactionCount = 0,
                favoriteCount = 0,
            ),
        )
    }
    is Video -> {
        CommonFeed(
            id = "android_video_${routeDest.id}",
            verb = "ANDROID_RECOMMENDATION",
            target = Feed.VideoTarget(
                id = routeDest.id,
                url = "https://www.zhihu.com/zvideo/${routeDest.id}",
                author = Person(
                    id = "",
                    url = "",
                    userType = "",
                    urlToken = null,
                    name = authorName,
                    headline = "",
                    avatarUrl = avatar,
                    isOrg = false,
                    gender = 0,
                    followersCount = 0,
                    isFollowing = false,
                    isFollowed = false,
                    badge = null,
                ),
                voteCount = -1,
                commentCount = 0,
                title = title,
                description = summary,
                excerpt = summary,
            ),
        )
    }
    else -> null
}
