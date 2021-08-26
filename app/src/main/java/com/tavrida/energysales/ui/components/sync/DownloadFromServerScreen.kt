package com.tavrida.energysales.ui.components.sync

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tavrida.energysales.ui.components.common.BackButton
import com.tavrida.energysales.ui.view_models.CounterReadingViewModel
import com.tavrida.utils.suppressedClickable
import com.tavrida.utils.Button
import com.tavrida.utils.iif
import com.tavrida.utils.rememberMutableStateOf
import kotlin.Exception

private enum class ScreenState {
    Confirmation, LoadingData, NothingUpdated, Done, Error
}

@Composable
fun DownloadFromServerScreen(
    viewModel: CounterReadingViewModel,
    onClose: () -> Unit
) {
    // confirm ->yes-> reloading ->
    //         ->no->  close

    var screenState by rememberMutableStateOf(ScreenState.Confirmation)
    var error: Exception? = null

    @Composable
    fun CloseButton() {
        Button("Закрыть", onClick = onClose)
    }

    @Composable
    fun Confirmation() {
        if (viewModel.numOfPendingItems() > 0) {
            Text("Информация на устройстве будет обновлена.")
            Text("Имеются несихронизированные данные.", color = Color.Red)
            Text("Продолжить?")
        } else {
            Text("Информация на устройстве будет обновлена.")
            Text("Продолжить?")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button("Нет", onClick = onClose)
            Spacer(modifier = Modifier.width(16.dp))
            Button("Да", onClick = { screenState = ScreenState.LoadingData })
        }
    }

    @Composable
    fun LoadingData() {
        LaunchedEffect(key1 = Unit) {
            screenState = try {
                val dataUpdated = viewModel.reloadDataFromServer()
                iif(dataUpdated, ScreenState.Done, ScreenState.NothingUpdated)
            } catch (e: Exception) {
                error = e
                ScreenState.Error
            }
        }

        CircularProgressIndicator()
        Text(text = "Выполняется обновление данных...")
    }

    @Composable
    fun Done() {
        Text(text = "Операция выполнилась успешно.")
        CloseButton()
    }

    @Composable
    fun Error() {
        Text(text = "Произошла ошибка!", color = Color.Red)
        Text(text = "Обратитесь к администратору!")
        Text(text = "Подробности: ${error!!.message}")
        CloseButton()
    }

    @Composable
    fun NothingUpdated() {
        Text(text = "На сервере нет информации для обновления.")
        Text(text = "Обновление не выполнилось.")
        CloseButton()
    }

    BackHandler(enabled = true, onBack = onClose)
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .suppressedClickable(),
        topBar = {
            BackButton(onClose)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (screenState) {
                ScreenState.Confirmation -> Confirmation()
                ScreenState.LoadingData -> LoadingData()
                ScreenState.NothingUpdated -> NothingUpdated()
                ScreenState.Done -> Done()
                ScreenState.Error -> Error()
                else -> throw Exception("Unexpected screenState")
            }
        }
    }

}


/*
@Composable
fun DownloadFromServerScreen(
    viewModel: CounterReadingViewModel,
    onClose: () -> Unit
) {
    var done by rememberMutableStateOf(false)
    var error by rememberMutableStateOf(null as Exception?)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    BackHandler(enabled = true, onBack = onClose)

    SideEffect {
        confirm(context, "Информация на устройстве будет обновлена. Продолжить?",
            no = onClose,
            yes = {
                scope.launch {
                    viewModel.reloadDataFromServer()
                }
            })
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
            if (true) {
                if (!done) {
                    CircularProgressIndicator()
                    Text(text = "Выполняется обновление данных...")
                } else {
                    if (error == null) {
                        Text(text = "Операция выполнилась успешно.")
                    } else {
                        Text(text = "Произошла ошибка!", color = Color.Red)
                        Text(text = "Обратитесь к администратору!")
                        Text(text = "Подробности: ${error!!.message}")
                    }
                    Button("Закрыть", onClick = onClose)
                }
            } else {
                Text("Нечего синхронизировать.")
                Button("Закрыть", onClick = onClose)
            }


        }
    }

}*/
