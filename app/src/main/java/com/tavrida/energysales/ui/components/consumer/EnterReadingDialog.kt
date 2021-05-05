package com.tavrida.energysales.ui.components.consumer

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import com.tavrida.energysales.data_access.models.Counter
import com.tavrida.energysales.data_access.models.CounterReading
import com.tavrida.energysales.ui.components.common.OutlinedDoubleField
import com.tavrida.utils.noTrailingZero
import kotlinx.coroutines.launch

@Composable
fun EnterReadingDialog(
    counter: Counter,
    currentReading: CounterReading?,
    onDismiss: () -> Unit,
    onNewReading: (Counter, Double) -> Unit
) {
    var newReading by remember { mutableStateOf(currentReading?.reading) }
    val context = LocalContext.current
    val onConfirm = { tryConfirm(counter, newReading, onNewReading, context) }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = counter.id) {
        launch { focusRequester.requestFocus() }
    }
    // TODO: make AutoFocusable wrapper
    /*
    @Composable
    fun AutoFocusable(content: @Composable AutoFocusScope.()->Unit){
    }

    */
    /*TODO("edit internal string value and convert to double ot confirmation step")
    TODO("isError = value.isNotEmpty() && value.isInvalidDouble()")
    TODO("confirm if value.isNotEmpy() && value.isValidDouble()")*/
    AlertDialog(
        text = {
            Column {
                Text("Показания для ${counter.serialNumber}:")
                Text("Пред. показ.: ${counter.prevReading.reading.noTrailingZero()}")
                OutlinedDoubleField(
                    modifier = Modifier.focusRequester(focusRequester),
                    value = newReading,
                    onValueChange = { newReading = it },
                    keyboardActions = KeyboardActions(
                        onDone = { onConfirm() }
                    )
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

private fun tryConfirm(
    counter: Counter,
    newReading: Double?,
    onNewReading: (Counter, Double) -> Unit,
    context: Context
) {
    if (newReading != null && newReading >= 0) {
        onNewReading(counter, newReading)
    } else {
        Toast.makeText(context, "Показания некорректны!", Toast.LENGTH_SHORT).show()
    }

}