package com.tavrida.energysales.ui.components

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.tavrida.energysales.data_access.models.Counter
import com.tavrida.energysales.data_access.models.CounterReading
import com.tavrida.energysales.ui.components.common.CircularBusyIndicator
import com.tavrida.energysales.ui.components.consumer.ConsumerDetailsScreen
import com.tavrida.energysales.ui.components.consumer.ConsumersListScreen
import com.tavrida.energysales.ui.components.counter.CounterQRCodeScanner
import com.tavrida.energysales.ui.components.counter.CounterScanner
import com.tavrida.energysales.ui.view_models.CounterReadingViewModel
import java.time.LocalDateTime

@Composable
fun App(viewModel: CounterReadingViewModel) {
    val context = LocalContext.current
    var scanByCamera by remember { mutableStateOf(false) }
    val selectedConsumerState = viewModel.selectedConsumer
    val showConsumerDetails = selectedConsumerState?.showDetails == true
    //hide search text field - because we need hide keyboard
    val searchFieldVisible = !(scanByCamera || showConsumerDetails)

    ConsumersListScreen(
        viewModel,
        searchFieldVisible = searchFieldVisible,
        onCounterScannerRequest = { scanByCamera = true })

    if (selectedConsumerState?.showDetails == true) {
        ConsumerDetailsScreen(
            consumerDetailsState = selectedConsumerState,
            onClose = {
                selectedConsumerState.selectedCounter = null
                selectedConsumerState.showDetails = false
            },
            onNewReading = { counter, newReading ->
                viewModel.applyNewReading(
                    counter,
                    newReading
                )
            },
            onCounterScannerRequest = { scanByCamera = true }
        )
    }

    if (scanByCamera) {
        CounterQRCodeScanner(
            onDismiss = { scanByCamera = false },
            onCounterSerialNumberReady = { sn ->
                scanByCamera = false
                val found = viewModel.activateCounterBySerialNumber(sn)
                if (!found) {
                    Toast.makeText(context, "Счетчик №($sn) не найден!", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    CircularBusyIndicator(viewModel.busy)
}