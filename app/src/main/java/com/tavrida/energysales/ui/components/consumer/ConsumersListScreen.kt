package com.tavrida.energysales.ui.components.consumer

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
    val scope = rememberCoroutineScope()
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    BackHandler {
        if (!scaffoldState.drawerState.isClosed) {
            scope.launch { scaffoldState.drawerState.close() }
        } else
            if (viewModel.search.query.isNotEmpty()) {
                viewModel.search.setQuery("", true)
            } else {
                confirm(activity, "Выйти?") {
                    activity.finish()
                }
            }
    }



    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {
                    Icon(imageVector = Icons.Outlined.Menu, contentDescription = null)
                }
                if (searchFieldVisible) {
                    ConsumerCounterSearch(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f), viewModel.search
                    )
                }
            }

        },
        floatingActionButton = {
            ScanByCameraFloatingButton(onCounterScannerRequest)
        },
        drawerGesturesEnabled = false,
        drawerContent = {
            SideMenu(onSyncWithServerClicked = { testMode -> })
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
private fun SideMenu(onSyncWithServerClicked: (testMode: Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
    ) {

        Row(
            modifier = Modifier
                .clickable { onSyncWithServerClicked(false) }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Outlined.Sync, contentDescription = null)
            Text(text = "Синхронизировать")
        }

        Row(
            modifier = Modifier
                .clickable { onSyncWithServerClicked(true) }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Outlined.Sync, contentDescription = null)
            Text(text = "Тест связи с сервером")
        }
    }
}

@Composable
private fun ConsumerCounterSearch(
    modifier: Modifier = Modifier,
    searchState: SearchState
) {
    OutlinedTextField(
        modifier = modifier,
        enabled = !searchState.searching,
        value = searchState.query,
        singleLine = true,
        onValueChange = {
            searchState.setQuery(it)
        },
        trailingIcon = {
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