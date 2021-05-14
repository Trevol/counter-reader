package com.tavrida.energysales.ui.components.consumer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.tavrida.energysales.ui.view_models.CounterReadingViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ConsumersListScreen(
    viewModel: CounterReadingViewModel,
    searchFieldVisible: Boolean,
    onCounterScannerRequest: () -> Unit
) {
    Scaffold(
        topBar = {
            if (searchFieldVisible) {
                ConsumerCounterSearchAndScanByCamera(
                    initialQuery = viewModel.searchQuery,
                    onQueryChange = { query ->
                        viewModel.searchQuery = query
                        viewModel.searchCustomers()
                    },
                    onCounterScannerRequest = onCounterScannerRequest
                )
            }
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
private fun ConsumerCounterSearchAndScanByCamera(
    initialQuery: String,
    onQueryChange: (String) -> Unit,
    searchDebounce: Long = 500,
    onCounterScannerRequest: () -> Unit
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
                delay(searchDebounce)
                searching = true
                onQueryChange(localQuery)
                searching = false
            }
        },
        leadingIcon = {
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
        },
        trailingIcon = {
            IconButton(
                modifier = Modifier.background(MaterialTheme.colors.secondary),
                onClick = onCounterScannerRequest
            ) {
                Icon(imageVector = Icons.Outlined.PhotoCamera, contentDescription = null)
            }
        }
    )
}
