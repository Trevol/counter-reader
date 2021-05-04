package com.tavrida.energysales.ui.components.consumer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tavrida.energysales.data_access.models.Consumer
import com.tavrida.energysales.ui.view_models.IndexedConsumer
import com.tavrida.utils.scrollToIfNotVisible

@Composable
fun ConsumersList(
    consumers: List<Consumer>,
    selectedConsumer: IndexedConsumer?,
    onClick: (IndexedConsumer) -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    SideEffect {
        listState.scrollToIfNotVisible(scope, selectedConsumer?.index, 5)
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(5.dp)
    ) {
        itemsIndexed(consumers) { index, consumer ->
            ConsumerListItem(
                consumer,
                isActive = selectedConsumer?.consumer === consumer,
                onClick = {
                    onClick(IndexedConsumer(consumer, index))
                }
            )
        }
    }
}

