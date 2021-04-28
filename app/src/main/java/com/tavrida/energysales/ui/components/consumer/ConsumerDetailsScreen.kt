package com.tavrida.energysales.ui.components.consumer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tavrida.energysales.data_access.models.Consumer
import com.tavrida.energysales.ui.components.common.BackButton
import com.tavrida.energysales.ui.components.counter.CountersItems
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
        topBar = { BackButton(onClick = onClose) }
    ) {
        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = consumer.name)
            CountersItems(consumer.counters)
        }
    }
}