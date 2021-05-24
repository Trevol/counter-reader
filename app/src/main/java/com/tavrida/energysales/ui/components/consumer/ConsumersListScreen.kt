package com.tavrida.energysales.ui.components.consumer

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.tavrida.energysales.ui.components.common.ScanByCameraFloatingButton
import com.tavrida.energysales.ui.view_models.CounterReadingViewModel
import com.tavrida.energysales.ui.view_models.SearchState
import com.tavrida.utils.confirm
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ConsumersListScreen(
    viewModel: CounterReadingViewModel,
    searchFieldVisible: Boolean,
    onCounterScannerRequest: () -> Unit
) {
    val activity = LocalContext.current as Activity
    BackHandler {
        if (viewModel.search.query.isNotEmpty()) {
            viewModel.search.setQuery("", true)
        } else {
            confirm(activity, "Выйти?") {
                activity.finish()
            }
        }
    }
    Scaffold(
        topBar = {
            if (searchFieldVisible) {
                ConsumerCounterSearch(viewModel.search)
            }
        },
        floatingActionButton = {
            ScanByCameraFloatingButton(onCounterScannerRequest)
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
private fun ConsumerCounterSearch(
    searchState: SearchState
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        enabled = !searchState.searching,
        value = searchState.query,
        onValueChange = {
            searchState.setQuery(it)
        },
        leadingIcon = {
            IconButton(
                enabled = !searchState.searching,
                onClick = {
                    searchState.setQuery("", true)
                }
            ) {
                Icon(imageVector = Icons.Outlined.Clear, contentDescription = null)
            }
        }
    )
}

@Composable
private fun ConsumerCounterSearch(
    initialQuery: String,
    onQueryChange: (String) -> Unit,
    searchDebounce: Long = 500
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
        }
    )
}
