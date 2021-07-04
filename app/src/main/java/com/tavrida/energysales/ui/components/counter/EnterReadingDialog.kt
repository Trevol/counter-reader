package com.tavrida.energysales.ui.components.counter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.tavrida.energysales.data_access.models.Counter
import com.tavrida.energysales.data_access.models.CounterReading
import com.tavrida.energysales.ui.components.common.AutoFocusable
import com.tavrida.utils.noTrailingZero
import com.tavrida.utils.Button

@Composable
fun EnterReadingDialog(
    counter: Counter,
    currentReading: CounterReading?,
    onDismiss: () -> Unit,
    onNewReading: (Counter, Double) -> Unit
) {
    var newReading by remember { mutableStateOf(currentReading?.reading.noTrailingZero()) }
    val onConfirm = { validateAndApply(counter, newReading, onNewReading) }
    val isError = newReading.isNotEmpty() && newReading.isInvalidDouble()

    @Composable
    fun DialogContent() {
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
    }

    @Composable
    fun DialogButtons() {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
        ) {
            Row(Modifier.fillMaxWidth()) {
                Button(text = "Отмена", onDismiss)
                Spacer(modifier = Modifier.weight(1f))
                Button(text = "Сохранить", onConfirm)
            }
        }
    }

    AlertDialog(
        text = {
            DialogContent()
        },
        onDismissRequest = onDismiss,
        buttons = {
            DialogButtons()
        },
        properties = DialogProperties(dismissOnClickOutside = false)
    )
}

private fun String.isInvalidDouble() = toDoubleOrNull() == null

private fun validateAndApply(
    counter: Counter,
    newReadingRawVal: String,
    onNewReading: (Counter, Double) -> Unit
) {
    val newReading = newReadingRawVal.toDoubleOrNull()
    if (newReading != null && newReading >= 0) {
        onNewReading(counter, newReading)
    }
}