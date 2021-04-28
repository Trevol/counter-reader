package com.tavrida.energysales.ui.components.counter

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tavrida.energysales.data_access.models.Counter

@Composable
fun CounterCard(modifier: Modifier = Modifier, counter: Counter) {
    val context = LocalContext.current
    Card(modifier = modifier, elevation = 10.dp) {
        CounterPropertyGrid(counter,
            modifier = Modifier.padding(10.dp),
            onCurrentReadingClick = {
                Toast.makeText(context, "Click!!! ${counter.serialNumber}", Toast.LENGTH_LONG)
                    .show()
            }
        )
    }
}