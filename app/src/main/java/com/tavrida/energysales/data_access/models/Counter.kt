package com.tavrida.energysales.data_access.models

import androidx.compose.runtime.snapshots.SnapshotStateList
import java.time.LocalDate
import java.time.LocalDateTime

data class Counter(
    val id: Int,
    val serialNumber: String,
    val consumerId: Int,
    val K: Double,
    val prevReading: PrevCounterReading,
    val readings: SnapshotStateList<CounterReading>,
    val comment: String? = null,
    val importOrder: Int
) {
    val recentReading: CounterReading?
        get() {
            val lastReading = readings.maxByOrNull { it.readingTime } ?: return null
            return if (lastReading.readingTime.isCurrentMonth()) {
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