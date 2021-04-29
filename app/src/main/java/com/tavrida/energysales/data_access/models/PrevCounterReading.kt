package com.tavrida.energysales.data_access.models

import java.time.LocalDate

data class PrevCounterReading(
    val id: Int,
    val counterId: Int,
    val reading: Double,
    val consumption: Double,
    val readDate: LocalDate,
    val comment: String? = null
)