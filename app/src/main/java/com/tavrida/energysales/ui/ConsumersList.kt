package com.tavrida.energysales.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tavrida.energysales.data_access.models.Consumer

@Composable
fun ConsumersList(consumers: List<Consumer>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(consumers) { consumer ->
            ConsumerCard(consumer)
        }
    }
}

@Composable
private fun ConsumerCard(consumer: Consumer) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { },
        elevation = 5.dp
    ) {
        Column {
            Text(text = consumer.name, fontSize = 24.sp)
            Row {
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    for (counter in consumer.counters) {
                        Text(text = counter.serialNumber.toString())
                    }
                }
            }
        }
    }
}