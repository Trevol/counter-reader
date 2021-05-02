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

private val ActiveColor = Color(0xFFF8B195)

@Composable
fun CounterCard(modifier: Modifier = Modifier, counter: Counter, isActive: Boolean, onReadingEditRequest: (Counter)-> Unit) {
    val color = if (isActive) ActiveColor else MaterialTheme.colors.surface

    Card(modifier = modifier, elevation = 10.dp, backgroundColor = color) {
        CounterPropertyGrid(counter,
            modifier = Modifier.padding(10.dp),
            onCurrentReadingClick = {
                onReadingEditRequest(counter)
            }
        )
    }
}