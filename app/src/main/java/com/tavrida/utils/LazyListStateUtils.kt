package com.tavrida.utils

import androidx.compose.foundation.lazy.LazyListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun LazyListState.isItemVisible(index: Int): Boolean {
    return visibleItemsIndexes()?.contains(index).orFalse()
}

fun LazyListState.visibleItemsIndexes() = layoutInfo.visibleItemsInfo.let {
    if (it.isEmpty())
        null
    else
        it.first().index..it.last().index
}

fun LazyListState.isItemNotVisible(index: Int) = !isItemVisible(index)

suspend fun LazyListState.scrollToIfNotVisible(index: Int?, scrollOffset: Int = 0) {
    //https://stackoverflow.com/questions/66712286/get-last-visible-item-index-in-jetpack-compose-lazycolumn
    if (index != null && isItemNotVisible(index)) {
        scrollToItem(index, scrollOffset)
    }
}

fun LazyListState.scrollToIfNotVisible(scope: CoroutineScope, index: Int?, scrollOffset: Int = 0) {
    scope.launch {
        scrollToIfNotVisible(index, scrollOffset)
    }
}