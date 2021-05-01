package com.tavrida.energysales.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.tavrida.energysales.ui.components.consumer.ConsumersList
import com.tavrida.energysales.ui.components.common.CircularBusyIndicator
import com.tavrida.energysales.ui.components.consumer.ConsumerDetailsScreen
import com.tavrida.energysales.ui.components.consumer.ConsumersListScreen
import com.tavrida.energysales.ui.view_models.CounterReadingViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun App(viewModel: CounterReadingViewModel) {
    val context = LocalContext.current
    var scanByCamera by remember { mutableStateOf(false) }

    ConsumersListScreen(viewModel, onCounterScannerRequest = { scanByCamera = true })

    if (viewModel.selectedConsumer != null) {
        ConsumerDetailsScreen(
            consumer = viewModel.selectedConsumer!!,
            activeCounter = viewModel.activeCounter,
            onClose = { viewModel.clearSelection() }
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
