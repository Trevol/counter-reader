package com.tavrida.energysales.ui.view_models

import androidx.compose.runtime.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CounterReadingInputState(counterId: Int? = null, reading: Float? = null) {
    var counterId by mutableStateOf(counterId)
    var reading by mutableStateOf(reading)

    fun clear() {
        counterId = null
        reading = null
    }

    val isValid get() = counterId != null && reading != null
}

