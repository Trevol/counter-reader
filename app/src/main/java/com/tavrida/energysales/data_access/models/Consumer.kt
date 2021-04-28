package com.tavrida.energysales.data_access.models

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

data class Consumer(
    val id: Int,
    val name: String,
    val counters: List<Counter> = listOf(),
    val comment: String? = null
)

data class Counter(
    val id: Int,
    val serialNumber: Int,
    val consumerId: Int,
    val K: Double,
    val prevReading: PrevCounterReading,
    val readings: List<CounterReading>,
    val comment: String? = null
) {
    val currentReading: CounterReading?
        get() {
            val lastReading = readings.sortedBy { it.readTime }.lastOrNull() ?: return null
            return if (lastReading.readTime.isCurrentMonth()) {
                lastReading
            } else {
                null
            }
        }

    private companion object {
        private fun LocalDateTime.isCurrentMonth() = LocalDate.now()
            .let { now ->
                year == now.year && monthValue == now.monthValue
            }
    }
}

data class PrevCounterReading(
    val id: Int,
    val counterId: Int,
    val reading: Double,
    val consumption: Double,
    val readDate: LocalDate,
    val comment: String? = null
)

data class CounterReading(
    val id: Int,
    val counterId: Int,
    val reading: Double,
    val readTime: LocalDateTime,
    val comment: String? = null
)

