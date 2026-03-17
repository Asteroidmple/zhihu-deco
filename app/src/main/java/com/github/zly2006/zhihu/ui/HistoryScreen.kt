package com.github.zly2006.zhihu.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.zly2006.zhihu.LocalNavigator
import com.github.zly2006.zhihu.ui.components.FeedCard
import com.github.zly2006.zhihu.ui.components.FeedPullToRefresh
import com.github.zly2006.zhihu.ui.components.PaginatedList
import com.github.zly2006.zhihu.viewmodel.feed.BaseFeedViewModel
import com.github.zly2006.zhihu.viewmodel.feed.HistoryViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(
    innerPadding: PaddingValues,
) {
    val navigator = LocalNavigator.current
    val viewModel: HistoryViewModel = viewModel()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val titles = listOf("全部", "回答", "文章", "问题", "用户", "想法", "视频")
    val pagerState = rememberPagerState(pageCount = { titles.size })

    LaunchedEffect(Unit) {
        if (viewModel.historyDisplayItems.isEmpty()) {
            viewModel.refresh(context)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        viewModel.selectedTabIndex = pagerState.currentPage
    }

    Column(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            val filteredItems = when (page) {
                0 -> viewModel.historyDisplayItems
                1 -> viewModel.historyDisplayItems.filter { item -> item.itemType == HistoryViewModel.ItemType.ANSWER }
                2 -> viewModel.historyDisplayItems.filter { item -> item.itemType == HistoryViewModel.ItemType.ARTICLE }
                3 -> viewModel.historyDisplayItems.filter { item -> item.itemType == HistoryViewModel.ItemType.QUESTION }
                4 -> viewModel.historyDisplayItems.filter { item -> item.itemType == HistoryViewModel.ItemType.PERSON }
                5 -> viewModel.historyDisplayItems.filter { item -> item.itemType == HistoryViewModel.ItemType.PIN }
                6 -> viewModel.historyDisplayItems.filter { item -> item.itemType == HistoryViewModel.ItemType.VIDEO }
                else -> viewModel.historyDisplayItems
            }

            FeedPullToRefresh(viewModel) {
                PaginatedList(
                    items = filteredItems,
                    onLoadMore = { },
                    isEnd = { true },
                ) { historyItem ->
                    FeedCard(
                        BaseFeedViewModel.FeedDisplayItem(
                            title = historyItem.title,
                            summary = historyItem.summary,
                            details = historyItem.details,
                            feed = historyItem.feed,
                            navDestination = historyItem.navDestination,
                            avatarSrc = historyItem.avatarSrc,
                            authorName = historyItem.authorName,
                        ),
                    ) {
                        historyItem.navDestination?.let { dest ->
                            navigator.onNavigate(dest)
                        }
                    }
                }
            }
        }
    }
}
