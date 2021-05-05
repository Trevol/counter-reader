package com.tavrida.energysales.ui.components.consumer

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.tavrida.energysales.data_access.models.Counter
import com.tavrida.energysales.data_access.models.CounterReading
import com.tavrida.energysales.ui.components.common.OutlinedDoubleField
import com.tavrida.utils.noTrailingZero
import kotlinx.coroutines.launch

/*
@Composable
fun EnterReadingDialog(
    counter: Counter,
    currentReading: CounterReading?,
    onDismiss: () -> Unit,
    onNewReading: (Counter, Double) -> Unit
) {
    var newReading by remember { mutableStateOf(currentReading?.reading.noTrailingZero()) }
    val context = LocalContext.current
    val onConfirm = { tryConfirm(counter, newReading, onNewReading, context) }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = counter.id) {
        launch { focusRequester.requestFocus() }
    }

    val isError = newReading.isNotEmpty() && newReading.isInvalidDouble()
    AlertDialog(
        text = {
            Column {
                Text("Показания для ${counter.serialNumber}:")
                Text("Пред. показ.: ${counter.prevReading.reading.noTrailingZero()}")
                OutlinedTextField(
                    modifier = Modifier.focusRequester(focusRequester),
                    value = newReading,
                    onValueChange = { newReading = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(
                        onDone = { onConfirm() }
                    ),
                    isError = isError,
                    label = {
                        val text = if (isError) "Наст. показ. НЕКОРРЕКТНЫ" else "Наст. показ."
                        Text(text)
                    }
                )
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            IconButton(
                onClick = { onConfirm() }
            ) {
                Icon(imageVector = Icons.Outlined.Done, contentDescription = "Сохранить")
            }
        },
        dismissButton = {
            IconButton(onClick = onDismiss) {
                Icon(imageVector = Icons.Outlined.Cancel, contentDescription = "Отмена")
            }
        }
    )
}
*/

@Composable
fun EnterReadingDialog(
    counter: Counter,
    currentReading: CounterReading?,
    onDismiss: () -> Unit,
    onNewReading: (Counter, Double) -> Unit
) {
    var newReading by remember { mutableStateOf(currentReading?.reading.noTrailingZero()) }
    val context = LocalContext.current
    val onConfirm = { tryConfirm(counter, newReading, onNewReading, context) }

    val isError = newReading.isNotEmpty() && newReading.isInvalidDouble()
    AlertDialog(
        text = {
            Column {
                Text("Показания для ${counter.serialNumber}:")
                Text("Пред. показ.: ${counter.prevReading.reading.noTrailingZero()}")
                AutoFocusable {
                    OutlinedTextField(
                        modifier = Modifier.focusRequester(),
                        value = newReading,
                        onValueChange = { newReading = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        keyboardActions = KeyboardActions(
                            onDone = { onConfirm() }
                        ),
                        isError = isError,
                        label = {
                            val text = if (isError) "Наст. показ. НЕКОРРЕКТНЫ" else "Наст. показ."
                            Text(text)
                        }
                    )
                }
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            IconButton(
                onClick = { onConfirm() }
            ) {
                Icon(imageVector = Icons.Outlined.Done, contentDescription = "Сохранить")
            }
        },
        dismissButton = {
            IconButton(onClick = onDismiss) {
                Icon(imageVector = Icons.Outlined.Cancel, contentDescription = "Отмена")
            }
        }
    )
}


class AutoFocusScope(private val focusRequester: FocusRequester = FocusRequester()) {
    fun requestFocus() = focusRequester.requestFocus()
    fun Modifier.focusRequester() = focusRequester(focusRequester)
}

@Composable
fun AutoFocusable(content: @Composable AutoFocusScope.() -> Unit) {
    val scope = remember { AutoFocusScope() }
    LaunchedEffect(key1 = Unit) {
        launch { scope.requestFocus() }
    }
    scope.content()
}

private fun String.isInvalidDouble() = toDoubleOrNull() == null

private fun tryConfirm(
    counter: Counter,
    newReadingRawVal: String,
    onNewReading: (Counter, Double) -> Unit,
    context: Context
) {
    val newReading = newReadingRawVal.toDoubleOrNull()
    if (newReading != null && newReading >= 0) {
        onNewReading(counter, newReading)
    } else {
        Toast.makeText(context, "Показания некорректны!", Toast.LENGTH_SHORT).show()
    }

}