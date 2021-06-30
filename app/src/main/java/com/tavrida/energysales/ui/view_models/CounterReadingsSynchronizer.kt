package com.tavrida.energysales.ui.view_models

import com.tavrida.energysales.apiClient.CounterReadingSyncApiClient
import com.tavrida.energysales.data_access.models.Consumer
import com.tavrida.energysales.data_access.models.CounterReading
import com.tavrida.energysales.data_access.models.IDataContext
import com.tavrida.energysales.data_contract.CounterReadingItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class CounterReadingsSynchronizer(
    val backendUrl: String?,
    val dataContext: IDataContext
) {
    init {
        if (backendUrl.isNullOrEmpty()){
            throw Exception("Backend URL is empty")
        }
    }
    suspend fun uploadReadingsToServer(allConsumers: List<Consumer>) {
        withContext(Dispatchers.IO) {
            val unsynchronized = allConsumers.unsynchronizedReadings()
            if (unsynchronized.isEmpty()) {
                return@withContext
            }

            val items = unsynchronized.map { it.toSyncItem() }
            val idMappings = apiClient(backendUrl!!).use {
                TODO("Change names and routes to upload/download")
                it.sync(items)
            }
            if (unsynchronized.size != idMappings.size) {
                throw Exception("unsynchronized.size != idMappings.size: ${unsynchronized.size} != ${idMappings.size}")
            }
            val now = LocalDateTime.now()
            for (r in unsynchronized) {
                val serverId =
                    idMappings.firstOrNull { it.id == r.id }?.serverId
                        ?: throw Exception("ServerId не найден для Показаний #${r.id}")
                r.synchronized = true
                r.syncTime = now
                r.serverId = serverId
            }
            dataContext.updateSyncData(unsynchronized)
        }
    }

    companion object {
        fun apiClient(backendUrl: String) =
            CounterReadingSyncApiClient(backendUrl)

        fun Iterable<Consumer>.unsynchronizedReadings() =
            flatMap { it.counters }.flatMap { it.readings }
                .filter { !it.synchronized }

        fun CounterReading.toSyncItem() = CounterReadingItem(
            id = id,
            user = user,
            counterId = counterId,
            reading = reading,
            readingTime = readingTime,
            comment = comment
        )
    }


}