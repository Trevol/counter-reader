package com.tavrida.energysales.ui.components.counter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.tavrida.energysales.data_access.models.Counter
import com.tavrida.energysales.data_access.models.CounterReading
import com.tavrida.energysales.ui.components.common.AutoFocusable
import com.tavrida.utils.noTrailingZero
import com.tavrida.utils.Button
import com.tavrida.utils.confirm

@Composable
fun EnterReadingDialog(
    counter: Counter,
    currentReading: CounterReading?,
    onDismiss: () -> Unit,
    onNewReading: (Counter, Double) -> Unit
) {
    val context = LocalContext.current
    var newReading by remember { mutableStateOf(currentReading?.reading.noTrailingZero()) }

    val isError = err(counter, newReading)

    val onSaveReading = {
        if (newReading.isNotEmpty() && !isError) {
            val warning: String? = warn(counter, newReading)
            if (warning != null) {
                confirm(context, message = warning, no = {}) {
                    validateAndApply(counter, newReading, onNewReading)
                }
            } else {
                validateAndApply(counter, newReading, onNewReading)
            }
        }
    }


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
                        onDone = { onSaveReading() }
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
                Button(text = "Сохранить", onSaveReading)
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

private fun err(counter: Counter, newReading: String): Boolean {
    return newReading.isNotEmpty() && newReading.isInvalidDouble() ||
            //if K==1 newReading should represent integer value
            counter.K == 1 && (newReading.contains(',') || newReading.contains('.'))
}

private fun warn(counter: Counter, newReadingRawVal: String): String? {
    val newReading = newReadingRawVal.toDoubleOrNull() ?: return "Некорректное значение"

    val prevReading = counter.prevReading.reading
    if (prevReading > newReading) {
        return "Подтвердите показания. Предыдущие показания больше текущих. Возможно, счетчик перешел цикл. Продолжить?"
    }
    if (prevReading == newReading) {
        return "Подтвердите показания. Текущие показания равны предыдущим. Продолжить?"
    }

    //TODO: брать за прошлый месяц, а не просто последний из истории
    val prevConsumption =
        counter.consumptionHistory.sortedBy { it.month }.lastOrNull()?.consumption ?: return null
    val currentConsumption = (newReading - prevReading) * counter.K

    if (currentConsumption < prevConsumption / 2) {
        return "Подтвердите показания. Текущее потребление ($currentConsumption) значительно меньше предыдущего ($prevConsumption). Продолжить?"
    }

    if (currentConsumption > prevConsumption * 2) {
        return "Подтвердите показания. Текущее потребление ($currentConsumption) значительно больше предыдущего ($prevConsumption). Продолжить?"
    }

    return null
}