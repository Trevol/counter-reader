package com.tavrida.energysales.ui.components.consumer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        Column(modifier = Modifier.padding(10.dp, 10.dp)) {
            Row {
                Text(
                    text = consumer.name,
                    fontSize = 24.sp
                )
                if (consumer.allCountersHaveRead()) {
                    DoneMark()
                }
            }
            ShortCountersList(consumer)
        }
    }
}

@Composable
fun ShortCountersList(consumer: Consumer) {
    Text(text = "Счетчики: ${consumer.countersInfo}")
}

@Composable
private inline fun DoneMark() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Icon(
            imageVector = Icons.Outlined.Done,
            contentDescription = "Сделано",
            tint = Color.Green
        )
    }
}