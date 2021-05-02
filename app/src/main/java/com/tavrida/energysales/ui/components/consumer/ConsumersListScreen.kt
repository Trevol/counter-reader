package com.tavrida.energysales.ui.components.consumer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.tavrida.energysales.ui.view_models.CounterReadingViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ConsumersListScreen(
    viewModel: CounterReadingViewModel,
    onCounterScannerRequest: () -> Unit
) {
    Scaffold(
        topBar = {
            ConsumerCounterSearch("", { query -> viewModel.searchCustomers(query) })
        },
        floatingActionButton = {
            ScanByCameraButton(onClick = onCounterScannerRequest)
        }
    ) {
        ConsumersList(
            viewModel.visibleConsumers,
            selectedConsumer = viewModel.selectedConsumer?.consumer,
            onClick = { viewModel.selectConsumer(it, showDetails = true) }
        )
    }
}


@Composable
private fun ScanByCameraButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick
    ) {
        Icon(Icons.Outlined.PhotoCamera, contentDescription = null)
    }
}

@Composable
private fun ConsumerCounterSearch(
    initialQuery: String,
    onQueryChange: (String) -> Unit,
    debounce: Long = 500
) {
    var searching by remember { mutableStateOf(false) }
    var localQuery by remember { mutableStateOf(initialQuery) }
    val scope = rememberCoroutineScope()
    var searchJob by remember { mutableStateOf<Job?>(null) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        enabled = !searching,
        value = localQuery,
        onValueChange = {
            localQuery = it
            searchJob?.cancel()
            searchJob = scope.launch {
                delay(debounce)
                searching = true
                onQueryChange(localQuery)
                searching = false
            }
        },
        leadingIcon = {
            Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
        },
        trailingIcon = {
            IconButton(
                enabled = !searching,
                onClick = {
                    localQuery = ""
                    searching = true
                    onQueryChange(localQuery)
                    searching = false
                }
            ) {
                Icon(imageVector = Icons.Outlined.Clear, contentDescription = null)
            }

        }
    )
}
