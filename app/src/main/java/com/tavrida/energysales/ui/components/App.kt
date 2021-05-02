package com.tavrida.energysales.ui.components

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.tavrida.energysales.ui.components.common.CircularBusyIndicator
import com.tavrida.energysales.ui.components.consumer.ConsumerDetailsScreen
import com.tavrida.energysales.ui.components.consumer.ConsumersListScreen
import com.tavrida.energysales.ui.components.counter.CounterScanner
import com.tavrida.energysales.ui.view_models.CounterReadingViewModel

@Composable
fun App(viewModel: CounterReadingViewModel) {
    val context = LocalContext.current
    var scanByCamera by remember { mutableStateOf(false) }

    ConsumersListScreen(viewModel, onCounterScannerRequest = { scanByCamera = true })

    val selectedConsumerState = viewModel.selectedConsumer
    if (selectedConsumerState?.showDetails == true) {
        ConsumerDetailsScreen(
            consumerDetailsState = selectedConsumerState,
            onClose = {
                selectedConsumerState.selectedCounter = null
                selectedConsumerState.showDetails = false
            }
        )
    }

    if (scanByCamera) {
        CounterScanner(
            onCounterSerialNumberReady = { sn ->
                scanByCamera = false
                val found = viewModel.activateCounterBySerialNumber(sn)
                if (!found) {
                    Toast.makeText(context, "Счетчик №($sn) не найден!", Toast.LENGTH_SHORT).show()
                }
            },
            onDismiss = { scanByCamera = false }
        )
    }

    CircularBusyIndicator(viewModel.busy)
}
