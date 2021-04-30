package com.tavrida.energysales.data_access.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.time.LocalDateTime

class CounterReading(
    val id: Int,
    val counterId: Int,
    reading: Double,
    readTime: LocalDateTime,
    comment: String? = null
) {
    var reading by mutableStateOf(reading)
    var readTime by mutableStateOf(readTime)
    var comment by mutableStateOf(comment)
}