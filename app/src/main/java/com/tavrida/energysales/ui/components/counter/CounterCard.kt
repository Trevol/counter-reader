package com.tavrida.energysales.ui.components.counter

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tavrida.energysales.data_access.models.Counter
import com.tavrida.energysales.data_access.models.CounterReading
import com.tavrida.energysales.ui.components.common.OutlinedDoubleField
import java.time.LocalDateTime

@Composable
fun CounterCard(modifier: Modifier = Modifier, counter: Counter, isActive: Boolean) {
    var inputReading by remember {
        mutableStateOf(isActive)
    }
    val color = if (isActive) Color(0xFFF8B195) else MaterialTheme.colors.surface

    Card(modifier = modifier, elevation = 10.dp, backgroundColor = color) {
        CounterPropertyGrid(counter,
            modifier = Modifier.padding(10.dp),
            onCurrentReadingClick = {
                inputReading = true
            }
        )
    }
    if (inputReading) {
        EnterReadingDialog(
            counter,
            counter.currentReading,
            onDismiss = {
                inputReading = false
            },
            onNewReading = { counter, newReading ->
                applyNewReading(counter, newReading)
                inputReading = false
            }
        )
    }
}

private fun applyNewReading(counter: Counter, newReading: Double) {
    // TODO("Save to Database!!!")
    val reading = counter.currentReading
    if (reading != null) {
        reading.reading = newReading
        reading.readTime = LocalDateTime.now()
    } else {
        counter.readings.add(
            CounterReading(-1, counter.id, newReading, LocalDateTime.now(), null)
        )
    }
}

@Composable
fun EnterReadingDialog(
    counter: Counter,
    currentReading: CounterReading?,
    onDismiss: () -> Unit,
    onNewReading: (Counter, Double) -> Unit
) {
    var reading by remember { mutableStateOf(currentReading?.reading) }
    val context = LocalContext.current

    fun tryConfirm() {
        val r = reading
        if (r != null && r >= 0) {
            onNewReading(counter, r)
        } else {
            Toast.makeText(context, "Показания некорректны!", Toast.LENGTH_SHORT).show()
        }

    }

    AlertDialog(
        text = {
            Column {
                Text("Показания для ${counter.serialNumber}:")
                Text("Пред. показ.: ${counter.prevReading.reading}")
                OutlinedDoubleField(
                    value = reading,
                    onValueChange = { reading = it },
                    keyboardActions = KeyboardActions(onDone = { tryConfirm() })
                )
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            IconButton(
                onClick = { tryConfirm() }
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