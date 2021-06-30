package com.tavrida.energysales.data_contract

import com.tavrida.utils.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class CounterReadingItem(
    val id: Int,
    val user: String,
    val counterId: Int,
    val reading: Double,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val readingTime: LocalDateTime,
    val comment: String? = null
)

