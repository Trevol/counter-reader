package com.tavrida.energysales.ui.components.consumer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tavrida.energysales.data_access.models.Consumer
import com.tavrida.utils.iif

@Composable
fun ConsumerListItem(
    consumer: Consumer,
    isActive: Boolean,
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
            Text(
                text = "${consumer.importOrder}. ${consumer.name}",
                fontSize = 24.sp
            )
            CountersList(consumer)
        }

        if (consumer.allCountersHaveRecentReadings()) {
            DoneMark(consumer.allCountersAreSynchronized())
        }
    }
}

@Composable
private inline fun CountersList(consumer: Consumer) {
    for (counter in consumer.counters) {
        Text(text = "${counter.serialNumber}(${counter.importOrder}). ${counter.comment.orEmpty()}")
    }

}

@Composable
private inline fun DoneMark(isSynchronized: Boolean) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Icon(
            imageVector = Icons.Outlined.Done,
            contentDescription = "Сделано",
            tint = isSynchronized.iif(Color.Green, Color.Red)
        )
    }
}