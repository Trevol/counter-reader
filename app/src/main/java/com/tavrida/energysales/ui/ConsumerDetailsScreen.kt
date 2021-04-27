package com.tavrida.energysales.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import com.tavrida.energysales.data_access.dbmodel.tables.CountersTable
import com.tavrida.energysales.data_access.models.Consumer
import com.tavrida.energysales.ui.components.BackHandler

@Composable
fun ConsumerDetailsScreen(
    consumer: Consumer,
    onClose: () -> Unit
) {
    BackHandler(onBack = { onClose() })
    Scaffold(
        topBar = {
            IconButton(onClick = { onClose() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    ) {
        Column {
            Text(text = "Details: ${consumer.name}")
            CountersTable()
        }

    }
}

@Composable
fun CountersTable() {
//DataTable for counters
//    https://github.com/hashlin/ComposeDataTable
//    https://proandroiddev.com/jetpack-compose-data-tables-33a247f59fd5
}
