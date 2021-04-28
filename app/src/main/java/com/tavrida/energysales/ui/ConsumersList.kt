package com.tavrida.energysales.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tavrida.energysales.data_access.models.Consumer

@Composable
fun ConsumersList(consumers: List<Consumer>, onClick: (Consumer) -> Unit) {
    var activeItem by remember { mutableStateOf<Consumer?>(null) }
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

@Composable
private fun ConsumerListItem(
    consumer: Consumer,
    isActive: Boolean = false,
    onClick: (Consumer) -> Unit
) {
    val color = if (isActive) Color(0xFFF8B195) else MaterialTheme.colors.surface
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { onClick(consumer) },
        elevation = 5.dp,
        backgroundColor = color
    ) {
        Text(
            modifier = Modifier.padding(10.dp, 20.dp),
            text = consumer.name,
            fontSize = 24.sp
        )
    }
}