package com.tavrida.energysales.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.tavrida.energysales.ui.components.common.BackButton
import com.tavrida.energysales.ui.components.common.OutlinedIntegerField
import com.tavrida.utils.suppressedClickable

@Composable
fun CounterScanner(onCounterSerialNumberReady: (Int) -> Unit, onDismiss: () -> Unit) {
    var serialNumberValue by remember { mutableStateOf(123 as Int?) }

    @Composable
    fun DoneButton() {
        Button(onClick = {
            val sn = serialNumberValue
            if (sn != null && sn > 0) {
                onCounterSerialNumberReady(sn)
            }
        }) {
            Text("Готово")
        }
    }

    BackHandler(onBack = onDismiss)
    Scaffold(
        modifier = Modifier.suppressedClickable(),
        topBar = {
            BackButton(onClick = onDismiss)
        }
    ) {
        Column {
            Text(text = "TODO: Сканирование камерой")
            OutlinedIntegerField(value = serialNumberValue, { serialNumberValue = it })
            DoneButton()
        }
    }
}