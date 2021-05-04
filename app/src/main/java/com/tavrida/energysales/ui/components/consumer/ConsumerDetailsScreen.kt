package com.tavrida.energysales.ui.components.consumer

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tavrida.energysales.data_access.models.Counter
import com.tavrida.energysales.data_access.models.CounterReading
import com.tavrida.energysales.ui.components.common.BackButton
import com.tavrida.energysales.ui.components.common.OutlinedDoubleField
import com.tavrida.energysales.ui.components.counter.CountersItems
import com.tavrida.energysales.ui.view_models.ConsumerDetailsState
import com.tavrida.utils.suppressedClickable
import java.time.LocalDateTime

@Composable
fun ConsumerDetailsScreen(
    consumerDetailsState: ConsumerDetailsState,
    onClose: () -> Unit
) {
    BackHandler(onBack = onClose)
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .suppressedClickable(),
        topBar = { BackButton(onClick = onClose) }
    ) {
        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = consumerDetailsState.consumer.consumer.name)
            CountersItems(
                consumerDetailsState.consumer.consumer.counters,
                selectedCounter = consumerDetailsState.selectedCounter?.counter,
                onReadingEditRequest = {
                    consumerDetailsState.selectCounter(it, true)
                }
            )
        }
    }

    if (consumerDetailsState.selectedCounter?.showReadingEditor == true) {
        val selectedCounter = consumerDetailsState.selectedCounter!!
        val counter = selectedCounter.counter
        EnterReadingDialog(
            counter,
            counter.currentReading,
            onDismiss = {
                selectedCounter.showReadingEditor = false
            },
            onNewReading = { counter, newReading ->
                applyNewReading(counter, newReading)
                selectedCounter.showReadingEditor = false
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