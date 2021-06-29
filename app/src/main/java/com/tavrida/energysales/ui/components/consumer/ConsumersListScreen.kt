package com.tavrida.energysales.ui.components.consumer

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.tavrida.energysales.ui.components.common.ScanByCameraFloatingButton
import com.tavrida.energysales.ui.view_models.CounterReadingViewModel
import com.tavrida.energysales.ui.view_models.SearchState
import com.tavrida.utils.ClickHandler
import com.tavrida.utils.confirm
import com.tavrida.utils.info
import kotlinx.coroutines.launch
import com.tavrida.utils.IconButton

@Composable
fun ConsumersListScreen(
    viewModel: CounterReadingViewModel,
    searchFieldVisible: Boolean,
    onCounterScannerRequest: ClickHandler,
    onUploadResultsToServer: ClickHandler,
    onDownloadFromServer: ClickHandler,
    onSettingsEditor: ClickHandler
) {
    val activity = LocalContext.current as Activity
    val scope = rememberCoroutineScope()
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    fun closeDrawer() = scope.launch { scaffoldState.drawerState.close() }
    fun openDrawer() = scope.launch { scaffoldState.drawerState.open() }
    fun closeDrawerOrClearFilterOrExitApp() {
        if (!scaffoldState.drawerState.isClosed) {
            closeDrawer()
        } else
            if (viewModel.search.query.isNotEmpty()) {
                viewModel.search.setQuery("", true)
            } else {
                confirm(activity, "Выйти?", yes = { activity.finish() })
            }
    }

    BackHandler(onBack = ::closeDrawerOrClearFilterOrExitApp)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(Icons.Outlined.Menu, onClick = { openDrawer() })
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
            ConsumersListScreenSideMenu(
                onDownloadFromServer = {
                    closeDrawer()
                    onDownloadFromServer()
                },
                onUploadResultsToServer = {
                    closeDrawer()
                    onUploadResultsToServer()
                },
                onProgressRequest = {
                    closeDrawer()
                    info(activity, viewModel.doneAndAllProgress())
                },
                onSettings = {
                    closeDrawer()
                    onSettingsEditor()
                }
            )
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
                Icons.Outlined.Clear,
                onClick = { searchState.setQuery("", true) },
                enabled = !searchState.searching
            )
        }
    )
}