package com.tavrida.energysales.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.tavrida.energysales.ui.components.CircularBusyIndicator
import com.tavrida.energysales.ui.view_models.CounterReadingViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun App(viewModel: CounterReadingViewModel) {
    ConsumersListScreen(viewModel)

    if (viewModel.selectedItem != null) {
        ConsumerDetailsScreen(
            consumer = viewModel.selectedItem!!,
            onClose = { viewModel.selectedItem = null }
        )
    }

    CircularBusyIndicator(viewModel.busy)
}

@Composable
private fun ConsumersListScreen(viewModel: CounterReadingViewModel) {
    Scaffold(
        topBar = {
            ConsumerCounterSearch("", { query -> viewModel.searchCustomers(query) })
        },
        floatingActionButton = {
            ScanByCameraButton()
        }
    ) {
        ConsumersList(viewModel.visibleCustomers, onClick = { viewModel.selectedItem = it })
    }
}


@Composable
private fun ScanByCameraButton() {
    FloatingActionButton(
        onClick = {
            //TODO: scan by camera and select consumer/counter
        }
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

