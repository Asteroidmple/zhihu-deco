package com.github.zly2006.zhihu.viewmodel.feed

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import com.github.zly2006.zhihu.Article
import com.github.zly2006.zhihu.ArticleType
import com.github.zly2006.zhihu.MainActivity
import com.github.zly2006.zhihu.NavDestination
import com.github.zly2006.zhihu.Person
import com.github.zly2006.zhihu.Pin
import com.github.zly2006.zhihu.Question
import com.github.zly2006.zhihu.Video
import com.github.zly2006.zhihu.data.CommonFeed
import com.github.zly2006.zhihu.data.Feed
import com.github.zly2006.zhihu.data.Person as DataPerson

class HistoryViewModel : BaseFeedViewModel() {
    companion object {
        private const val TAG = "HistoryViewModel"
    }

    enum class ItemType {
        ANSWER,
        ARTICLE,
        QUESTION,
        PERSON,
        PIN,
        VIDEO,
        OTHER,
    }

    data class HistoryDisplayItem(
        val title: String,
        val summary: String?,
        val details: String,
        val feed: Feed?,
        val navDestination: NavDestination?,
        val avatarSrc: String? = null,
        val authorName: String? = null,
        val itemType: ItemType,
    )

    var historyDisplayItems = mutableStateListOf<HistoryDisplayItem>()
    var selectedTabIndex by mutableIntStateOf(0)

    override val initialUrl: String
        get() = error("不需要 URL")

    override val isEnd: Boolean
        get() = historyDisplayItems.isNotEmpty()

    override fun refresh(context: Context) {
        if (isLoading) return
        isLoading = true
        errorMessage = null

        val history = (context as MainActivity).history
        historyDisplayItems.clear()
        displayItems.clear()

        Log.d(TAG, "开始加载历史记录，总数：${history.history.size}")

        history.history.forEachIndexed { index, dest ->
            val displayItem = createHistoryDisplayItem(dest)
            displayItem?.let { item ->
                historyDisplayItems.add(item)
                // 同时添加到父类的displayItems以保持兼容性
                displayItems.add(
                    FeedDisplayItem(
                        title = item.title,
                        summary = item.summary,
                        details = item.details,
                        feed = item.feed,
                        navDestination = item.navDestination,
                        avatarSrc = item.avatarSrc,
                        authorName = item.authorName,
                    ),
                )
                Log.d(TAG, "[$index] 创建 Feed: ${dest::class.simpleName}, type: ${item.itemType}")
            } ?: run {
                Log.w(TAG, "[$index] 无法创建 FeedDisplayItem: ${dest::class.simpleName}")
            }
        }

        Log.d(TAG, "历史记录加载完成，displayItems 数量：${historyDisplayItems.size}")
        isLoading = false
    }

    private fun createHistoryDisplayItem(dest: NavDestination): HistoryDisplayItem? = when (dest) {
        is Article -> {
            if (dest.type == ArticleType.Answer) {
                // 回答 - 创建 AnswerTarget
                val answerTarget = Feed.AnswerTarget(
                    id = dest.id,
                    url = "https://www.zhihu.com/answer/${dest.id}",
                    author = null,
                    createdTime = System.currentTimeMillis(),
                    updatedTime = System.currentTimeMillis(),
                    voteupCount = 0,
                    thanksCount = 0,
                    commentCount = 0,
                    isCopyable = true,
                    question = Feed.QuestionTarget(
                        id = 0,
                        _title = dest.title,
                        url = "https://www.zhihu.com/question/0",
                        type = "question",
                        answerCount = 0,
                        commentCount = 0,
                        followerCount = 0,
                        isFollowing = false,
                    ),
                    excerpt = dest.excerpt.orEmpty(),
                )
                val feed = CommonFeed(
                    id = "history_answer_${dest.id}",
                    verb = "ANSWER_CREATE",
                    target = answerTarget,
                )

                HistoryDisplayItem(
                    title = dest.title,
                    summary = dest.excerpt ?: "",
                    details = "回答",
                    authorName = dest.authorName,
                    feed = feed,
                    avatarSrc = dest.avatarSrc,
                    navDestination = dest,
                    itemType = ItemType.ANSWER,
                )
            } else {
                // 文章 - 创建 ArticleTarget
                val articleTarget = Feed.ArticleTarget(
                    id = dest.id,
                    url = "https://zhuanlan.zhihu.com/p/${dest.id}",
                    author = DataPerson(
                        id = "author_${dest.id}",
                        url = "",
                        userType = "people",
                        urlToken = "",
                        name = dest.authorName.orEmpty(),
                        headline = "",
                        avatarUrl = dest.avatarSrc.orEmpty(),
                        isOrg = false,
                        gender = 0,
                        followersCount = 0,
                        isFollowing = false,
                        isFollowed = false,
                        badge = null,
                    ),
                    voteupCount = 0,
                    commentCount = 0,
                    title = dest.title,
                    excerpt = dest.excerpt.orEmpty(),
                    content = "",
                    created = System.currentTimeMillis(),
                    updated = System.currentTimeMillis(),
                    isLabeled = false,
                    visitedCount = 0,
                    favoriteCount = 0,
                )
                val feed = CommonFeed(
                    id = "history_article_${dest.id}",
                    verb = "ARTICLE_CREATE",
                    target = articleTarget,
                )

                HistoryDisplayItem(
                    title = dest.title,
                    summary = dest.excerpt ?: "",
                    details = "文章",
                    authorName = dest.authorName,
                    feed = feed,
                    avatarSrc = dest.avatarSrc,
                    navDestination = dest,
                    itemType = ItemType.ARTICLE,
                )
            }
        }
        is Question -> {
            // 问题 - 创建 QuestionTarget
            val questionTarget = Feed.QuestionTarget(
                id = dest.questionId,
                _title = dest.title,
                url = "https://www.zhihu.com/question/${dest.questionId}",
                type = "question",
                answerCount = 0,
                commentCount = 0,
                followerCount = 0,
                isFollowing = false,
            )
            val feed = CommonFeed(
                id = "history_question_${dest.questionId}",
                verb = "QUESTION_CREATE",
                target = questionTarget,
            )

            HistoryDisplayItem(
                title = dest.title,
                details = "问题",
                feed = feed,
                navDestination = dest,
                summary = "",
                itemType = ItemType.QUESTION,
            )
        }
        is Person -> {
            // 用户 - 创建 PinTarget
            val pinTarget = Feed.PinTarget(
                id = dest.id.toLongOrNull() ?: 0,
                url = "https://www.zhihu.com/people/${dest.urlToken}",
                author = DataPerson(
                    id = dest.id,
                    url = "",
                    userType = "people",
                    urlToken = dest.urlToken,
                    name = dest.name,
                    headline = "",
                    avatarUrl = "",
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
            )
            val feed = CommonFeed(
                id = "history_person_${dest.id}",
                verb = "PEOPLE_CREATE",
                target = pinTarget,
            )

            HistoryDisplayItem(
                title = dest.name,
                details = "用户",
                feed = feed,
                navDestination = dest,
                summary = "",
                itemType = ItemType.PERSON,
            )
        }
        is Pin -> {
            // 想法
            HistoryDisplayItem(
                title = "想法",
                details = "想法",
                feed = null,
                navDestination = dest,
                summary = "",
                itemType = ItemType.PIN,
            )
        }
        is Video -> {
            // 视频
            HistoryDisplayItem(
                title = "视频",
                details = "视频",
                feed = null,
                navDestination = dest,
                summary = "",
                itemType = ItemType.VIDEO,
            )
        }
        else -> {
            Log.w(TAG, "未知的 NavDestination 类型：${dest::class.simpleName}")
            null
        }
    }

    override suspend fun fetchFeeds(context: Context) {
    }

    override fun loadMore(context: Context) {
        // 不需要 loadMore，所有数据一次性加载
    }
}
