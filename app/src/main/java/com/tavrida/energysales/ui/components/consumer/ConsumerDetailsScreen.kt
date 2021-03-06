package com.tavrida.energysales.ui.components.consumer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tavrida.energysales.data_access.models.Counter
import com.tavrida.energysales.ui.components.common.BackButton
import com.tavrida.energysales.ui.components.common.ScanByCameraFloatingButton
import com.tavrida.energysales.ui.components.counter.CountersItems
import com.tavrida.energysales.ui.components.counter.EnterReadingDialog
import com.tavrida.energysales.ui.view_models.ConsumerDetailsState
import com.tavrida.utils.suppressedClickable

@Composable
fun ConsumerDetailsScreen(
    consumerDetailsState: ConsumerDetailsState,
    onClose: () -> Unit,
    onNewReading: (Counter, Double) -> Unit,
    onCounterScannerRequest: () -> Unit
) {
    BackHandler(onBack = onClose)
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .suppressedClickable(),
        topBar = {
            BackButton(onClose)
        },
        floatingActionButton = {
            ScanByCameraFloatingButton(onCounterScannerRequest)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = consumerDetailsState.consumer.consumer.name)
            CountersItems(
                consumerDetailsState.consumer.consumer.counters,
                selectedCounter = consumerDetailsState.selectedCounter?.counter,
                onReadingEditRequest = {
                    consumerDetailsState.selectCounter(it, true)
                }
            )
        }
    }

    if (consumerDetailsState.selectedCounter?.showReadingEditor == true) {
        val selectedCounter = consumerDetailsState.selectedCounter!!
        val counter = selectedCounter.counter
        EnterReadingDialog(
            counter,
            counter.recentReading,
            onDismiss = {
                selectedCounter.showReadingEditor = false
            },
            onNewReading = { counter, newReading ->
                onNewReading(counter, newReading)
                selectedCounter.showReadingEditor = false
            }
        )
    }
}