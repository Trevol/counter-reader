package com.tavrida.energysales.data_access.models

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.tavrida.utils.toEpochMilli
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
            val lastReading = readings.maxByOrNull { it.readingTime }
            return lastReading
            /*
            val lastReading = readings.maxByOrNull { it.readingTime } ?: return null
            return if (lastReading.readingTime.isCurrentMonth()) {
                lastReading
            } else {
                null
            }*/
        }

    private companion object {
        //24h * 60 (min in h) * 60(sec in min) * 1000
        private const val msInDay = 24 * 60 * 60 * 1000
        private fun LocalDateTime.withinDays(n: Int) {

            LocalDateTime.now().toEpochMilli()
        }

        private fun LocalDateTime.isCurrentMonth() = LocalDate.now()
            .let { now ->
                year == now.year && monthValue == now.monthValue
            }
    }
}