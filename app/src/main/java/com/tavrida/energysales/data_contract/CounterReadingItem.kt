package com.tavrida.energysales.data_contract

import kotlinx.serialization.Serializable

@Serializable
data class CounterReadingItem(
    val id: Int,
    val user: String,
    val counterId: Int,
    val reading: Double,
    val readingTime: Long,
    val comment: String? = null
)

