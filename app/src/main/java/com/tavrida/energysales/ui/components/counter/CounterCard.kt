package com.tavrida.energysales.ui.components.counter

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
        title = "Показания для ${counter.serialNumber}".text(),
        text = {
            OutlinedDoubleField(value = reading, onValueChange = { reading = it })
        }
    )
}