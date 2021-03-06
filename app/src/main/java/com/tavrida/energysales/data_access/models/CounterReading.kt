package com.tavrida.energysales.data_access.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.time.LocalDateTime

class CounterReading(
    var id: Int,
    val counterId: Int,
    reading: Double,
    readingTime: LocalDateTime,
    val user: String,
    comment: String?,
    synchronized: Boolean,
    syncTime: LocalDateTime?,
    serverId: Int?
) {
    var reading by mutableStateOf(reading)
    var readingTime by mutableStateOf(readingTime)
    var comment by mutableStateOf(comment)

    var synchronized by mutableStateOf(synchronized)
    var syncTime by mutableStateOf(syncTime)
    var serverId by mutableStateOf(serverId)
}