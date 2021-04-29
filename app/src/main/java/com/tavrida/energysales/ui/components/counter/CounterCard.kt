package com.tavrida.energysales.ui.components.counter

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.tavrida.energysales.data_access.models.Counter
import com.tavrida.energysales.ui.components.common.OutlinedDoubleField
import com.tavrida.energysales.ui.components.common.text

@Composable
fun CounterCard(modifier: Modifier = Modifier, counter: Counter) {
    var inputReading by remember {
        mutableStateOf(false)
    }
    Card(modifier = modifier, elevation = 10.dp) {
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
            onDismiss = {
                inputReading = false
            },
            onNewReading = { counter, reading ->
                inputReading = false
            }
        )
    }
}

@Composable
fun EnterReadingDialog(
    counter: Counter,
    onDismiss: () -> Unit,
    onNewReading: (Counter, Double) -> Unit
) {
    var reading by remember { mutableStateOf(null as Double?) }
    val context = LocalContext.current

    AlertDialog(
        text = {
            Column {
                Text("Показания для ${counter.serialNumber}:")
                Text("Пред. показ.: ${counter.prevReading.reading}")
                OutlinedDoubleField(
                    value = reading,
                    onValueChange = { reading = it }
                )
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            IconButton(
                onClick = {
                    val r = reading
                    if (r != null && r >= 0) {
                        onNewReading(counter, r)
                    } else {
                        Toast.makeText(context, "Показания некорректны!", Toast.LENGTH_SHORT).show()
                    }
                }
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