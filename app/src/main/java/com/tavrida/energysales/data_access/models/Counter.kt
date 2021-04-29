package com.tavrida.energysales.data_access.models

import java.time.LocalDate
import java.time.LocalDateTime

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