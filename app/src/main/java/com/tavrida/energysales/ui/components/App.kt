package com.tavrida.energysales.ui.components

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.tavrida.energysales.data_access.models.Counter
import com.tavrida.energysales.data_access.models.CounterReading
import com.tavrida.energysales.ui.components.common.BackButton
import com.tavrida.energysales.ui.components.common.CircularBusyIndicator
import com.tavrida.energysales.ui.components.consumer.ConsumerDetailsScreen
import com.tavrida.energysales.ui.components.consumer.ConsumersListScreen
import com.tavrida.energysales.ui.components.counter.CounterQRCodeScanner
import com.tavrida.energysales.ui.components.counter.CounterScanner
import com.tavrida.energysales.ui.view_models.CounterReadingViewModel
import com.tavrida.utils.printlnStamped
import com.tavrida.utils.rememberMutableStateOf
import com.tavrida.utils.suppressedClickable
import java.time.LocalDateTime

@Composable
fun App(viewModel: CounterReadingViewModel) {
    val context = LocalContext.current
    var scanByCamera by remember { mutableStateOf(false) }
    val selectedConsumerState = viewModel.selectedConsumer
    val showConsumerDetails = selectedConsumerState?.showDetails == true
    var syncWithServer by rememberMutableStateOf(null as SyncWithServerRequest?)

    //hide search text field - because we need hide keyboard
    val searchFieldVisible = !(scanByCamera || showConsumerDetails || syncWithServer != null)

    ConsumersListScreen(
        viewModel,
        searchFieldVisible = searchFieldVisible,
        onCounterScannerRequest = { scanByCamera = true },
        onSyncWithServerRequest = { testMode -> syncWithServer = SyncWithServerRequest(testMode) })

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

    if (syncWithServer != null) {
        SyncWithServerScreen(
            testMode = syncWithServer!!.testMode,
            onClose = { syncWithServer = null })
    }

    CircularBusyIndicator(viewModel.busy)
}

private data class SyncWithServerRequest(val testMode: Boolean)

@Composable
fun SyncWithServerScreen(testMode: Boolean, onClose: () -> Unit) {
    BackHandler(onBack = onClose)
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .suppressedClickable(),
        topBar = {
            BackButton(onClose)
        }
    ) {
        Text("Sync!!! testMode=$testMode")
    }

}