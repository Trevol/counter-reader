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
private inline fun ShortCountersList(consumer: Consumer) {
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