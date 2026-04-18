package com.github.zly2006.zhihu.ui.components

import android.content.Context.MODE_PRIVATE
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.github.zly2006.zhihu.Account
import com.github.zly2006.zhihu.LocalNavigator
import com.github.zly2006.zhihu.data.Feed
import com.github.zly2006.zhihu.data.target
import com.github.zly2006.zhihu.theme.NeutralGray500
import com.github.zly2006.zhihu.theme.ThemeManager
import com.github.zly2006.zhihu.theme.ZhihuBlue
import com.github.zly2006.zhihu.theme.ZhihuGreen
import com.github.zly2006.zhihu.theme.ZhihuOrange
import com.github.zly2006.zhihu.theme.ZhihuRed
import com.github.zly2006.zhihu.ui.PREFERENCE_NAME
import com.github.zly2006.zhihu.util.parseHtmlTextWithTheme
import com.github.zly2006.zhihu.viewmodel.feed.BaseFeedViewModel
import com.github.zly2006.zhihu.viewmodel.feed.BaseFeedViewModel.FeedDisplayItem
import com.github.zly2006.zhihu.BuildConfig
import android.util.Log
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@Composable
fun FeedCard(
    item: BaseFeedViewModel.FeedDisplayItem,
    modifier: Modifier = Modifier,
    maxHeight: Dp = 240.dp,
    thumbnailUrl: String? = null,
    horizontalPadding: Dp = 16.dp,
    onLike: ((BaseFeedViewModel.FeedDisplayItem) -> Unit)? = null,
    onDislike: ((BaseFeedViewModel.FeedDisplayItem) -> Unit)? = null,
    onBlockUser: ((BaseFeedViewModel.FeedDisplayItem) -> Unit)? = null,
    onBlockByKeywords: ((BaseFeedViewModel.FeedDisplayItem) -> Unit)? = null,
    onBlockTopic: ((topicId: String, topicName: String) -> Unit)? = null,
    onClick: BaseFeedViewModel.FeedDisplayItem.() -> Unit,
) {
    val density = LocalDensity.current
    val context = LocalContext.current
    var offsetX by remember { mutableFloatStateOf(0f) }
    var currentY by remember { mutableFloatStateOf(0f) }
    var startY by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val preferences = remember { context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE) }
    val enableSwipeReaction = remember {
        preferences.getBoolean("enableSwipeReaction", false)
    } &&
        onLike != null &&
        onDislike != null
    val showFeedThumbnail = remember {
        preferences.getBoolean("showFeedThumbnail", true)
    }
    val feedCardStyle = remember {
        preferences.getString("feedCardStyle", "card")
    }
    val duo3CardAppearance = remember { preferences.getBoolean("duo3_card_appearance", false) }
    val duo3CardLayout = remember { preferences.getBoolean("duo3_card_layout", false) }

    val animatedOffsetX by animateFloatAsState(
        targetValue = if (isDragging) offsetX else 0f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = 0,
        ),
        label = "offsetAnimation",
    )

    val actionAlpha by animateFloatAsState(
        targetValue = if (abs(animatedOffsetX) > 50f) (abs(animatedOffsetX) - 50f) / 100f else 0f,
        animationSpec = tween(150),
        label = "actionAlpha",
    )

    val currentAction = when {
        abs(animatedOffsetX) < 75f -> "none"
        currentY - startY < -30f -> "like"
        currentY - startY > 30f -> "dislike"
        else -> "neutral"
    }

    val actionColors = remember { FeedActionColors() }

    if (feedCardStyle == "divider") {
        Column(
            modifier = modifier.fillMaxWidth().heightIn(max = maxHeight),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(item) }
                    .padding(horizontal = horizontalPadding, vertical = 12.dp),
            ) {
                FeedCardContent(
                    item = item,
                    showFeedThumbnail = showFeedThumbnail,
                    thumbnailUrl = thumbnailUrl,
                    showMenu = showMenu,
                    onShowMenuChange = { showMenu = it },
                    onBlockUser = onBlockUser,
                    onBlockByKeywords = onBlockByKeywords,
                    onBlockTopic = onBlockTopic,
                    duo3CardLayout = duo3CardLayout,
                )
            }
            HorizontalDivider(thickness = 0.3.dp)
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(max = maxHeight)
                .padding(horizontal = horizontalPadding, vertical = 8.dp),
        ) {
            Card(
                colors = if (duo3CardAppearance) {
                    CardDefaults.cardColors().copy(
                        containerColor = if (ThemeManager.isDarkTheme()) {
                            MaterialTheme.colorScheme.surfaceContainerHigh
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                    )
                } else {
                    CardDefaults.cardColors()
                },
                shape = if (duo3CardAppearance) {
                    RoundedCornerShape(24.dp)
                } else {
                    CardDefaults.shape
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(1 - min(actionAlpha, 0.5f))
                    .offset(x = with(density) { animatedOffsetX.toDp() })
                    .let { if (duo3CardAppearance) it.clip(RoundedCornerShape(24.dp)) else it }
                    .clickable {
                        if (!isDragging && abs(animatedOffsetX) < 10f) {
                            onClick(item)
                        }
                    }.let {
                        if (enableSwipeReaction) {
                            it.pointerInput(Unit) {
                                detectHorizontalDragGestures(
                                    onDragStart = { offset ->
                                        isDragging = true
                                        startY = offset.y
                                        currentY = offset.y
                                    },
                                    onDragEnd = {
                                        isDragging = false
                                        when {
                                            abs(offsetX) >= 75f && currentY - startY < -30f -> {
                                                onLike(item)
                                            }
                                            abs(offsetX) >= 75f && currentY - startY > 30f -> {
                                                onDislike(item)
                                            }
                                        }
                                        coroutineScope.launch {
                                            offsetX = 0f
                                            currentY = 0f
                                            startY = 0f
                                        }
                                    },
                                ) { change, dragAmount ->
                                    currentY = change.position.y
                                    val newOffset = offsetX + dragAmount
                                    offsetX = max(newOffset, -250f).coerceAtMost(0f)
                                }
                            }
                        } else {
                            it
                        }
                    },
                elevation = if (duo3CardAppearance) {
                    CardDefaults.cardElevation()
                } else {
                    CardDefaults.cardElevation(defaultElevation = if (isDragging) 8.dp else 2.dp)
                },
            ) {
                Column(
                    modifier = if (duo3CardAppearance) {
                        Modifier.padding(16.dp, 12.dp, 16.dp, 16.dp)
                    } else {
                        Modifier.padding(8.dp)
                    },
                ) {
                    FeedCardContent(
                        item = item,
                        showFeedThumbnail = showFeedThumbnail,
                        thumbnailUrl = thumbnailUrl,
                        showMenu = showMenu,
                        onShowMenuChange = { showMenu = it },
                        onBlockUser = onBlockUser,
                        onBlockByKeywords = onBlockByKeywords,
                        onBlockTopic = onBlockTopic,
                        duo3CardLayout = duo3CardLayout,
                    )
                }
            }

            if (actionAlpha > 0f && enableSwipeReaction) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            color = when (currentAction) {
                                "like" -> actionColors.likeBackground.copy(alpha = actionAlpha * 0.2f)
                                "dislike" -> actionColors.dislikeBackground.copy(alpha = actionAlpha * 0.2f)
                                "neutral" -> actionColors.neutralBackground.copy(alpha = actionAlpha * 0.1f)
                                else -> Color.Transparent
                            },
                            shape = RoundedCornerShape(12.dp),
                        ),
                    contentAlignment = when (currentAction) {
                        "like" -> Alignment.TopStart
                        "dislike" -> Alignment.BottomStart
                        else -> Alignment.CenterStart
                    },
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                    ) {
                        when (currentAction) {
                            "like" -> {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "喜欢",
                                    tint = actionColors.likeIcon,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .scale(1f + actionAlpha * 0.3f),
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "向上滑动 - 喜欢",
                                    color = actionColors.likeText,
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.scale(1f + actionAlpha * 0.2f),
                                )
                            }
                            "dislike" -> {
                                Icon(
                                    imageVector = Icons.Default.ThumbDown,
                                    contentDescription = "不喜欢",
                                    tint = actionColors.dislikeIcon,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .scale(1f + actionAlpha * 0.3f),
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "向下滑动 - 不喜欢",
                                    color = actionColors.dislikeText,
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.scale(1f + actionAlpha * 0.2f),
                                )
                            }
                            "neutral" -> {
                                Text(
                                    text = "上下滑动选择",
                                    color = actionColors.neutralText,
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.scale(1f + actionAlpha * 0.2f),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private class FeedActionColors {
    val likeBackground: Color
        @Composable get() = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    
    val dislikeBackground: Color
        @Composable get() = MaterialTheme.colorScheme.error.copy(alpha = 0.08f)
    
    val neutralBackground: Color
        @Composable get() = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
    
    val likeIcon: Color
        @Composable get() = MaterialTheme.colorScheme.primary
    
    val dislikeIcon: Color
        @Composable get() = MaterialTheme.colorScheme.error
    
    val likeText: Color
        @Composable get() = MaterialTheme.colorScheme.primary
    
    val dislikeText: Color
        @Composable get() = MaterialTheme.colorScheme.error
    
    val neutralText: Color
        @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant
}

@Composable
private fun FeedCardMenuBox(
    item: BaseFeedViewModel.FeedDisplayItem,
    showMenu: Boolean,
    onShowMenuChange: (Boolean) -> Unit,
    onBlockUser: ((BaseFeedViewModel.FeedDisplayItem) -> Unit)?,
    onBlockByKeywords: ((BaseFeedViewModel.FeedDisplayItem) -> Unit)?,
    onBlockTopic: ((topicId: String, topicName: String) -> Unit)?,
    navigator: com.github.zly2006.zhihu.Navigator,
) {
    Box {
        IconButton(
            onClick = { onShowMenuChange(true) },
            modifier = Modifier
                .size(48.dp)
                .semantics { contentDescription = "更多选项" },
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp),
            )
        }
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { onShowMenuChange(false) },
        ) {
            DropdownMenuContent(
                item = item,
                onShowMenuChange = onShowMenuChange,
                onBlockUser = onBlockUser,
                onBlockByKeywords = onBlockByKeywords,
                onBlockTopic = onBlockTopic,
                navigator = navigator,
            )
        }
    }
}

@Composable
private fun DropdownMenuContent(
    item: BaseFeedViewModel.FeedDisplayItem,
    onShowMenuChange: (Boolean) -> Unit,
    onBlockUser: ((BaseFeedViewModel.FeedDisplayItem) -> Unit)?,
    onBlockByKeywords: ((BaseFeedViewModel.FeedDisplayItem) -> Unit)?,
    onBlockTopic: ((topicId: String, topicName: String) -> Unit)?,
    navigator: com.github.zly2006.zhihu.Navigator,
) {
    @Suppress("SimplifyBooleanWithConstants", "KotlinConstantConditions")
    if (onBlockByKeywords != null && !BuildConfig.IS_LITE) {
        DropdownMenuItem(
            text = { Text("按关键词屏蔽") },
            onClick = {
                onShowMenuChange(false)
                onBlockByKeywords(item)
            },
        )
    }
    DropdownMenuItem(
        text = { Text("屏蔽用户") },
        onClick = {
            onShowMenuChange(false)
            onBlockUser?.invoke(item)
        },
    )
    if (onBlockTopic != null && item.raw != null) {
        val topics = when (val raw = item.raw) {
            is com.github.zly2006.zhihu.data.DataHolder.Answer -> raw.question.topics
            is com.github.zly2006.zhihu.data.DataHolder.Question -> raw.topics
            is com.github.zly2006.zhihu.data.DataHolder.Article -> raw.topics ?: emptyList()
            else -> emptyList()
        }
        topics.forEach { topic ->
            DropdownMenuItem(
                text = { Text("屏蔽「${topic.name}」") },
                onClick = {
                    onShowMenuChange(false)
                    onBlockTopic(topic.id, topic.name)
                },
            )
        }
    }
    DropdownMenuItem(
        text = { Text("外观设置") },
        onClick = {
            onShowMenuChange(false)
            navigator.onNavigate(Account.AppearanceSettings())
        },
    )
    if (item.isFiltered) {
        DropdownMenuItem(
            text = { Text("不再屏蔽低赞内容") },
            onClick = {
                onShowMenuChange(false)
                navigator.onNavigate(Account.RecommendSettings("enableQualityFilter"))
            },
        )
    }
}

@Composable
private fun FeedCardContent(
    item: BaseFeedViewModel.FeedDisplayItem,
    showFeedThumbnail: Boolean,
    thumbnailUrl: String?,
    showMenu: Boolean,
    onShowMenuChange: (Boolean) -> Unit,
    onBlockUser: ((BaseFeedViewModel.FeedDisplayItem) -> Unit)?,
    onBlockByKeywords: ((BaseFeedViewModel.FeedDisplayItem) -> Unit)?,
    onBlockTopic: ((topicId: String, topicName: String) -> Unit)?,
    duo3CardLayout: Boolean,
) {
    val navigator = LocalNavigator.current
    if (duo3CardLayout) {
        if (!item.title.isEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val tagInfo = getTagInfo(item.feed, item)
                Text(
                    text = tagInfo.text,
                    fontSize = 12.sp,
                    color = tagInfo.textColor,
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .background(tagInfo.backgroundColor, RoundedCornerShape(4.dp)),
                )
                Text(
                    text = parseHtmlTextWithTheme(item.title),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
        }

        Column {
            Row {
                Text(
                    text = parseHtmlTextWithTheme(item.summary ?: ""),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                if (!thumbnailUrl.isNullOrEmpty() && showFeedThumbnail && !item.isFiltered) {
                    Spacer(modifier = Modifier.width(8.dp))
                    AsyncImage(
                        model = thumbnailUrl,
                        contentDescription = "缩略图",
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .sizeIn(maxHeight = 80.dp, maxWidth = 128.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.FillHeight,
                    )
                }
            }
            if (item.details.isNotEmpty() || (item.avatarSrc != null && item.authorName != null)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (item.avatarSrc != null && item.authorName != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {},
                        ) {
                            item.avatarSrc.let {
                                AsyncImage(
                                    model = it,
                                    contentDescription = "作者头像",
                                    modifier = Modifier.clip(CircleShape).size(24.dp),
                                )
                                Spacer(Modifier.width(8.dp))
                            }
                            Text(
                                text = item.authorName,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        Spacer(Modifier.width(6.dp))
                    }
                    if (item.details.isNotEmpty()) {
                        Text(
                            text = item.details,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    FeedCardMenuBox(item, showMenu, onShowMenuChange, onBlockUser, onBlockByKeywords, onBlockTopic, navigator)
                }
            }
        }
    } else {
        if (!item.title.isEmpty() && !item.isFiltered) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val tagInfo = getTagInfo(item.feed, item)
                Text(
                    text = tagInfo.text,
                    fontSize = 12.sp,
                    color = tagInfo.textColor,
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .background(tagInfo.backgroundColor, RoundedCornerShape(4.dp)),
                )
                Text(
                    text = parseHtmlTextWithTheme(item.title),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        if (item.avatarSrc != null && item.authorName != null) {
            Spacer(Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {},
            ) {
                item.avatarSrc.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "作者头像",
                        modifier = Modifier.clip(CircleShape).size(20.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text = item.authorName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }

    Column {
        if (!item.summary.isNullOrBlank()) {
            Text(
                text = parseHtmlTextWithTheme(item.summary),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(
                    top = if (item.isFiltered) 0.dp else 4.dp,
                ),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (item.details.isNotEmpty()) {
                Text(
                    text = item.details,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
            }

            Box {
                IconButton(
                    onClick = { onShowMenuChange(true) },
                    modifier = Modifier
                        .size(48.dp)
                        .semantics { contentDescription = "更多选项" },
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp),
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { onShowMenuChange(false) },
                ) {
                    DropdownMenuContent(
                        item = item,
                        onShowMenuChange = onShowMenuChange,
                        onBlockUser = onBlockUser,
                        onBlockByKeywords = onBlockByKeywords,
                        onBlockTopic = onBlockTopic,
                        navigator = navigator,
                    )
                }
            }
        }

        if (!thumbnailUrl.isNullOrEmpty() && showFeedThumbnail) {
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = "缩略图",
                modifier = Modifier
                    .size(60.dp, 60.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
        }
    }
}

private data class TagInfo(
    val text: String,
    val backgroundColor: Color,
    val textColor: Color = Color.White,
)

private fun getTagInfo(feed: Feed?, item: FeedDisplayItem): TagInfo {
    if (feed != null) {
        val target = feed.target

        if (target != null) {
            val typeName = target::class.simpleName ?: ""
            return when {
                typeName.contains("Answer") -> TagInfo("回答", ZhihuBlue)
                typeName.contains("Article") -> TagInfo("文章", ZhihuGreen)
                typeName.contains("Video") -> TagInfo("视频", ZhihuOrange)
                typeName.contains("Pin") -> TagInfo("想法", ZhihuRed)
                typeName.contains("Question") -> TagInfo("问题", ZhihuBlue)
                else -> {
                    Log.d("FeedCard", "getTagInfo: unknown target type=$typeName")
                    TagInfo("其他", NeutralGray500)
                }
            }
        }
    }

    val details = item.details
    return when {
        details.contains("回答") -> TagInfo("回答", ZhihuBlue)
        details.contains("文章") -> TagInfo("文章", ZhihuGreen)
        details.contains("视频") -> TagInfo("视频", ZhihuOrange)
        details.contains("想法") -> TagInfo("想法", ZhihuRed)
        details.contains("问题") -> TagInfo("问题", ZhihuBlue)
        else -> TagInfo("其他", NeutralGray500)
    }
}
