package com.tavrida.energysales.ui

import android.R
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.tavrida.energysales.data_access.models.Consumer
import com.tavrida.energysales.ui.components.CircularBusyIndicator
import com.tavrida.energysales.ui.view_models.CounterReadingViewModel

@Composable
fun AppScreen(viewModel: CounterReadingViewModel) {
    Scaffold(
        topBar = {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "dddd",
                onValueChange = {},
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(Icons.Outlined.PhotoCamera, contentDescription = null)
            }
        }
    ) {
        ConsumersList(viewModel.consumers)
        CircularBusyIndicator(viewModel.busy)
    }
}

@Composable
fun ConsumersList(consumers: List<Consumer>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(consumers) { consumer ->
            Text(text = consumer.name)
        }
    }
}

