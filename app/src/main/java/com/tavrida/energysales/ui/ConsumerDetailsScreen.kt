package com.tavrida.energysales.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tavrida.energysales.data_access.models.Consumer
import com.tavrida.energysales.data_access.models.Counter
import com.tavrida.utils.suppressedClickable

@Composable
fun ConsumerDetailsScreen(
    consumer: Consumer,
    onClose: () -> Unit
) {
    BackHandler(onBack = onClose)
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .suppressedClickable(),
        topBar = {
            IconButton(onClick = onClose) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    ) {
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(text = consumer.name)
            CountersItems(consumer.counters)
        }

    }
}

@Composable
fun CountersItems(counters: List<Counter>) {
    counters.forEach {
        CounterCard(
            counter = it,
            modifier = Modifier.padding(10.dp, 5.dp).fillMaxWidth()
        )
    }
}

@Composable
fun CounterCard(modifier: Modifier = Modifier, counter: Counter) {
    // serialNumber  Заводской №
    // K             К трансф.
    // comment       Примечание
    // prevReading.consumption Расход
    // prevReading.reading     Пред. показ.
    // reading  (current month) Наст. показ

    val propWidth = Modifier.width(100.dp)
    Card(modifier = modifier, elevation = 10.dp) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row {
                Text(modifier = propWidth, text = "Заводской №")
                Text(counter.serialNumber.toString())
            }
            Row {
                Text(modifier = propWidth, text = "К трансф")
                Text(counter.K.toString())
            }
            Row {
                Text(modifier = propWidth, text = "Примечание")
                Text(counter.comment.orEmpty())
            }

            Row {
                Text(modifier = propWidth, text = "Расход")
                Text(counter.prevReading.consumption.toString())
            }

            Row {
                Text(modifier = propWidth, text = "Пред. показ.")
                Text(counter.prevReading.reading.toString())
            }

            Row {
                Text(modifier = propWidth, text = "Наст. показ.")
                // reading  (current month) Наст. показ
                Text(counter.currentReading?.reading?.toString().orEmpty())
            }
        }
    }
}
