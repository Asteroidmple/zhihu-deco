package com.github.zly2006.zhihu.viewmodel.feed

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.zly2006.zhihu.data.AccountData
import com.github.zly2006.zhihu.data.Feed
import com.github.zly2006.zhihu.data.target
import com.github.zly2006.zhihu.ui.IHomeFeedViewModel
import com.github.zly2006.zhihu.ui.PREFERENCE_NAME
import com.github.zly2006.zhihu.util.signFetchRequest
import com.github.zly2006.zhihu.viewmodel.filter.ContentFilterExtensions
import com.github.zly2006.zhihu.viewmodel.filter.ContentType
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray

class HomeFeedViewModel :
    BaseFeedViewModel(),
    IHomeFeedViewModel {
    override val initialUrl: String
//        get() = "https://www.zhihu.com/api/v3/feed/topstory/recommend?desktop=true&limit=10"
        get() = "https://api.zhihu.com/topstory/recommend"

    init {
        allowGuestAccess = true
    }

    public override suspend fun fetchFeeds(context: Context) {
        markItemsAsTouched(context)
        super.fetchFeeds(context)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun processResponse(context: Context, data: List<Feed>, rawData: JsonArray) {
        allData.addAll(data)
        debugData.addAll(rawData)

        viewModelScope.launch {
            val newItems = data
                .flatten()
                .filter { feed -> feed.target?.navDestination != null }
                .map { feed -> createDisplayItem(context, feed) }

            val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

            // 后台运行内容过滤
            val filteredItems = ContentFilterExtensions.applyContentFilterToDisplayItems(context, newItems)
            val newDestinations = filteredItems.map { it.navDestination }.toSet()

            // 记录内容展示
            recordContentDisplays(context, filteredItems)

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
        }
    }

    /**
     * 记录用户与内容的交互行为
     * 应该在用户点击、点赞等操作时调用
     */
    override suspend fun recordContentInteraction(context: Context, feed: Feed) {
        withContext(Dispatchers.IO) {
            try {
                when (val target = feed.target) {
                    is Feed.AnswerTarget -> {
                        ContentFilterExtensions.recordContentInteraction(
                            context,
                            ContentType.ANSWER,
                            target.id.toString(),
                        )
                    }
                    is Feed.ArticleTarget -> {
                        ContentFilterExtensions.recordContentInteraction(
                            context,
                            ContentType.ARTICLE,
                            target.id.toString(),
                        )
                    }
                    is Feed.QuestionTarget -> {
                        ContentFilterExtensions.recordContentInteraction(
                            context,
                            ContentType.QUESTION,
                            target.id.toString(),
                        )
                    }
                    else -> {
                        // 其他类型暂不处理
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeFeedViewModel", "Failed to record content interaction", e)
            }
        }
    }

    /**
     * 记录用户点击内容
     * 在viewModelScope中运行，使用viewModelScope代替GlobalScope
     */
    override fun onUiContentClick(context: Context, feed: Feed, item: BaseFeedViewModel.FeedDisplayItem) {
        viewModelScope.launch(Dispatchers.IO) {
            sendReadStatusToServer(context, feed)
            recordContentInteraction(context, feed)
        }
    }

    private suspend fun markItemsAsTouched(context: Context, httpClient: HttpClient = AccountData.httpClient(context)) {
        try {
            val untouchedAnswers = displayItems
                .filter { !it.isFiltered && it.feed?.target is Feed.AnswerTarget }

            if (untouchedAnswers.isNotEmpty()) {
                httpClient
                    .post("https://www.zhihu.com/lastread/touch") {
                        header("x-requested-with", "fetch")
                        signFetchRequest()
                        setBody(
                            MultiPartFormDataContent(
                                formData {
                                    append(
                                        "items",
                                        buildJsonArray {
                                            untouchedAnswers.forEach { item ->
                                                item.feed?.let { feed ->
                                                    when (val target = feed.target) {
                                                        is Feed.AnswerTarget -> {
                                                            add(
                                                                buildJsonArray {
                                                                    add("answer")
                                                                    add(target.id.toString())
                                                                    add("touch")
                                                                },
                                                            )
                                                        }

                                                        is Feed.ArticleTarget -> {
                                                            add(
                                                                buildJsonArray {
                                                                    add("article")
                                                                    add(target.id.toString())
                                                                    add("touch")
                                                                },
                                                            )
                                                        }

                                                        else -> {}
                                                    }
                                                }
                                            }
                                        }.toString(),
                                    )
                                },
                            ),
                        )
                    }.let { response ->
                        if (!response.status.isSuccess()) {
                            Log.e("Browse-Touch", response.bodyAsText())
                        }
                    }
            }
        } catch (e: Exception) {
            Log.e("FeedViewModel", "Failed to mark items as touched", e)
        }
    }
}
