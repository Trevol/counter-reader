package com.tavrida.energysales.ui.components.counter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.tavrida.energysales.data_access.models.Counter
import com.tavrida.energysales.data_access.models.CounterReading
import com.tavrida.energysales.ui.components.common.AutoFocusable
import com.tavrida.utils.noTrailingZero
import com.tavrida.utils.IconButton

@Composable
fun EnterReadingDialog(
    counter: Counter,
    currentReading: CounterReading?,
    onDismiss: () -> Unit,
    onNewReading: (Counter, Double) -> Unit
) {
    var newReading by remember { mutableStateOf(currentReading?.reading.noTrailingZero()) }
    val onConfirm = { valiadteAndApply(counter, newReading, onNewReading) }
    val isError = newReading.isNotEmpty() && newReading.isInvalidDouble()
    AlertDialog(
        text = {
            Column {
                Text("Показания для ${counter.serialNumber}:")
                Text("Пред. показ.: ${counter.prevReading.reading.noTrailingZero()}")
                Text("ImportOrder: ${counter.importOrder}")
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
                        },
                        singleLine = true,
                        maxLines = 1,
                    )
                }
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            IconButton(Icons.Outlined.Done, onClick = onConfirm)
        }
    )
}

private fun String.isInvalidDouble() = toDoubleOrNull() == null

private fun valiadteAndApply(
    counter: Counter,
    newReadingRawVal: String,
    onNewReading: (Counter, Double) -> Unit
) {
    val newReading = newReadingRawVal.toDoubleOrNull()
    if (newReading != null && newReading >= 0) {
        onNewReading(counter, newReading)
    }
}