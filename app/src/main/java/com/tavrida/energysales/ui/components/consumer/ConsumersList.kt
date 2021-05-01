package com.tavrida.energysales.ui.components.consumer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tavrida.energysales.data_access.models.Consumer

@Composable
fun ConsumersList(consumers: List<Consumer>, selectedConsumer: Consumer?, onClick: (Consumer) -> Unit) {
    var activeItem by remember { mutableStateOf(selectedConsumer) }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(5.dp)
    ) {
        items(consumers) { consumer ->
            ConsumerListItem(
                consumer,
                isActive = activeItem == consumer,
                onClick = {
                    activeItem = consumer
                    onClick(consumer)
                }
            )
        }
    }
}

