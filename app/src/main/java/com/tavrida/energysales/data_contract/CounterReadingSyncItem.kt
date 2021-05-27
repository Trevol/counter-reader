package com.tavrida.energysales.data_contract

import kotlinx.serialization.Serializable

@Serializable
data class CounterReadingSyncItem(
    val id: Int,
    val user: String,
    val counterId: Int,
    val reading: Double,
    val readingTime: Long,
    val comment: String?
)

