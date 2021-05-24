package com.tavrida.energysales.data_access.models

data class Consumer(
    val id: Int,
    val name: String,
    var counters: MutableList<Counter> = mutableListOf(),
    val comment: String? = null,
    val importOrder: Int
) {
    val countersInfo =
        counters.sortedBy { it.serialNumber }.map { "${it.serialNumber}(${it.importOrder})" }
            .joinToString(separator = ", ")

    fun allCountersHaveRead() = counters.all { it.currentReading != null }
}