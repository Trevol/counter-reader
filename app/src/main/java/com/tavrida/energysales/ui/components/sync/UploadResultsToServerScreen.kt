package com.tavrida.energysales.ui.components.sync

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tavrida.energysales.ui.components.common.BackButton
import com.tavrida.energysales.ui.view_models.CounterReadingViewModel
import com.tavrida.utils.rememberMutableStateOf
import com.tavrida.utils.suppressedClickable
import java.lang.Exception

@Composable
fun UploadResultsToServerScreen(
    viewModel: CounterReadingViewModel,
    onClose: () -> Unit
) {
    var done by rememberMutableStateOf(false)
    var error by rememberMutableStateOf(null as Exception?)
    val numOfUnsyncItems = remember { viewModel.numOfUnsyncItems() }

    BackHandler(enabled = true, onBack = onClose)

    if (numOfUnsyncItems > 0) {
        LaunchedEffect(key1 = Unit) {
            try {
                viewModel.syncWithServer()
            } catch (e: Exception) {
                error = e
            }
            done = true
        }
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .suppressedClickable(),
        topBar = {
            BackButton(onClose)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (numOfUnsyncItems > 0) {
                if (!done) {
                    CircularProgressIndicator()
                    Text(text = "Выполняется синхронизация...")
                } else {
                    if (error == null) {
                        Text(text = "Синхронизация выполнилась успешно.")
                    } else {
                        Text(text = "Произошла ошибка!", color = Color.Red)
                        Text(text = "Обратитесь к администратору!")
                        Text(text = "Подробности: ${error!!.message}")
                    }
                    Button(onClick = onClose) {
                        Text(text = "Закрыть")
                    }
                }
            } else {
                Text(text = "Нечего синхронизировать.")
                Button(onClick = onClose) {
                    Text(text = "Закрыть")
                }
            }


        }
    }

}