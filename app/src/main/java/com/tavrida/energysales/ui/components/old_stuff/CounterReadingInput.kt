package com.tavrida.energysales.ui.components.old_stuff

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tavrida.energysales.ui.components.common.*
import com.tavrida.energysales.ui.theme.CounterReaderTheme
import com.tavrida.energysales.ui.view_models.CounterReadingViewModel

@Composable
fun CounterReadingInput(viewModel: CounterReadingViewModel) {
    CounterReaderTheme {
        OrientationSelector(
            portrait = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Content(viewModel)
                }
            },
            landscape = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Content(viewModel)
                }
            }
        )
        CircularBusyIndicator(viewModel.busy)
    }
}

@Composable
private fun Content(viewModel: CounterReadingViewModel) {
    OutlinedIntegerField(
        value = viewModel.counterReading.counterId,
        onValueChange = { viewModel.counterReading.counterId = it },
        label = "Счетчик".text()
    )

    Spacer(modifier = Modifier.width(4.dp))

    OutlinedFloatField(
        value = viewModel.counterReading.reading,
        onValueChange = { viewModel.counterReading.reading = it },
        label = "Показания".text()
    )

    Spacer(modifier = Modifier.width(4.dp))

    Button(
        onClick = { viewModel.save() },
        enabled = viewModel.canSave()
    ) {
        Text("Сохранить")
    }
}