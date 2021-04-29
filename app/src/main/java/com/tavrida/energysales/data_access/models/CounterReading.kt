package com.tavrida.energysales.data_access.models

import java.time.LocalDateTime

data class CounterReading(
    val id: Int,
    val counterId: Int,
    val reading: Double,
    val readTime: LocalDateTime,
    val comment: String? = null
)