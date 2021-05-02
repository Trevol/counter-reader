package com.tavrida.energysales.ui.components.counter

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tavrida.energysales.data_access.models.Counter

@Composable
fun CountersItems(
    counters: List<Counter>,
    selectedCounter: Counter?,
    onReadingEditRequest: (Counter) -> Unit
) {
    counters.forEach {
        CounterCard(
            modifier = Modifier
                .padding(10.dp, 5.dp)
                .fillMaxWidth(),
            counter = it,
            isActive = it == selectedCounter,
            onReadingEditRequest = onReadingEditRequest
        )
    }
}