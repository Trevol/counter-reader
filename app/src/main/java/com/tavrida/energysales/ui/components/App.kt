package com.tavrida.energysales.ui.components

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.tavrida.energysales.ui.components.common.CircularBusyIndicator
import com.tavrida.energysales.ui.components.consumer.ConsumerDetailsScreen
import com.tavrida.energysales.ui.components.consumer.ConsumersListScreen
import com.tavrida.energysales.ui.components.counter.CounterQRCodeScanner
import com.tavrida.energysales.ui.components.sync.DownloadFromServerScreen
import com.tavrida.energysales.ui.components.sync.UploadResultsToServerScreen
import com.tavrida.energysales.ui.view_models.CounterReadingViewModel
import com.tavrida.utils.rememberMutableStateOf

@Composable
fun App(viewModel: CounterReadingViewModel) {
    val context = LocalContext.current
    val selectedConsumerState = viewModel.selectedConsumer

    val showConsumerDetails = selectedConsumerState?.showDetails == true
    var scanByCameraMode by rememberMutableStateOf(false)
    var uploadToServerMode by rememberMutableStateOf(false)
    var downloadFromServerMode by rememberMutableStateOf(false)
    var settingsEditorMode by rememberMutableStateOf(false)

    //hide search text field - because we need hide keyboard
    val searchFieldVisible = !(scanByCameraMode || showConsumerDetails || uploadToServerMode)

    ConsumersListScreen(
        viewModel,
        searchFieldVisible = searchFieldVisible,
        onCounterScannerRequest = { scanByCameraMode = true },
        onUploadResultsToServer = { uploadToServerMode = true },
        onDownloadFromServer = { downloadFromServerMode = true },
        onSettingsEditor = { settingsEditorMode = true }
    )

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
            onCounterScannerRequest = { scanByCameraMode = true }
        )
    }

    if (scanByCameraMode) {
        CounterQRCodeScanner(
            onDismiss = { scanByCameraMode = false },
            onCounterSerialNumberReady = { sn ->
                scanByCameraMode = false
                val found = viewModel.activateCounterBySerialNumber(sn)
                if (!found) {
                    Toast.makeText(context, "Счетчик №($sn) не найден!", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    if (uploadToServerMode) {
        UploadResultsToServerScreen(
            viewModel = viewModel,
            onClose = { uploadToServerMode = false }
        )
    }

    if (downloadFromServerMode) {
        DownloadFromServerScreen(
            viewModel = viewModel,
            onClose = { downloadFromServerMode = false }
        )
    }

    if (settingsEditorMode) {
        AppSettingsEditorDialog(onDismiss = { settingsEditorMode = false })
    }

    CircularBusyIndicator(viewModel.busy)
}

