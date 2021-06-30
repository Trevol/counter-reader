package com.tavrida.energysales.data_contract

import com.tavrida.utils.serializers.LocalDateIso8601Serializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class ConsumerData(
    val id: Int,
    val name: String,
    var counters: List<CounterData>,
    val comment: String?,
    val importOrder: Int
)

@Serializable
data class CounterData(
    val id: Int,
    val serialNumber: String,
    val consumerId: Int,
    val K: Double,
    val prevReading: PrevCounterReadingData,
    val comment: String? = null,
    val importOrder: Int
)

@Serializable
data class PrevCounterReadingData(
    val id: Int,
    val counterId: Int,
    val reading: Double,
    val consumption: Double,
    @Serializable(with = LocalDateIso8601Serializer::class)
    val readDate: LocalDate,
    val comment: String? = null
)