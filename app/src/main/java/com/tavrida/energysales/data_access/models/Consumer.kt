package com.tavrida.energysales.data_access.models

data class Consumer(
    val id: Int,
    val name: String,
    val counters: List<Counter> = listOf(),
    val comment: String? = null
) {
    val countersInfo = counters.sortedBy { it.serialNumber }.map { it.serialNumber }.joinToString(separator = ", ")
    fun allCountersHaveRead() = counters.all { it.currentReading != null }
}

