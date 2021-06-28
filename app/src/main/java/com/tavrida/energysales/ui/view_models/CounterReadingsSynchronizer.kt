package com.tavrida.energysales.ui.view_models

import com.tavrida.energysales.AppSettings
import com.tavrida.energysales.apiClient.CounterReadingSyncApiClient
import com.tavrida.energysales.data_access.models.Consumer
import com.tavrida.energysales.data_access.models.CounterReading
import com.tavrida.energysales.data_access.models.IDataContext
import com.tavrida.energysales.data_contract.CounterReadingSyncItem
import com.tavrida.utils.toEpochMilli
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class CounterReadingsSynchronizer(val dataContext: IDataContext) {
    suspend fun sync(allConsumers: List<Consumer>, testMode: Boolean) {
        if (testMode) {
            syncInTestMode(allConsumers)
        } else {
            syncInRealMode(allConsumers)
        }
    }

    private suspend fun syncInRealMode(allConsumers: List<Consumer>) {
        withContext(Dispatchers.IO) {
            val unsynchronized = allConsumers.unsynchronizedReadings()
            if (unsynchronized.isEmpty()) {
                return@withContext
            }

            val items = unsynchronized.map { it.toSyncItem() }
            val idMappings = apiClient().use {
                it.sync(items, testMode = false)
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


    private suspend fun syncInTestMode(allConsumers: List<Consumer>) {
        withContext(Dispatchers.IO) {
            val items = (1..250).map {
                CounterReadingSyncItem(
                    id = 1,
                    user = "Саша",
                    counterId = it % 190 + 1,
                    reading = 999.0,
                    readingTime = LocalDateTime.now().toEpochMilli(),
                    comment = null
                )
            }
            val idMappings = apiClient().use {
                it.sync(items, testMode = true)
            }
            if (items.size != idMappings.size) {
                throw Exception("unsynchronized.size != idMappings.size: ${items.size} != ${idMappings.size}")
            }
        }

        /*withContext(Dispatchers.IO) {
            val unsynchronized = allConsumers.unsynchronizedReadings()
            if (unsynchronized.isEmpty()) {
                return@withContext
            }

            val items = unsynchronized.map { it.toSyncItem() }
            val idMappings = apiClient().use {
                it.sync(items, testMode = true)
            }
            if (unsynchronized.size != idMappings.size) {
                throw Exception("unsynchronized.size != idMappings.size: ${unsynchronized.size} != ${idMappings.size}")
            }
        }*/
    }

    companion object {
        fun apiClient() =
            CounterReadingSyncApiClient(AppSettings.backendHost, AppSettings.backendPort)

        fun Iterable<Consumer>.unsynchronizedReadings() =
            flatMap { it.counters }.flatMap { it.readings }
                .filter { !it.synchronized }

        fun CounterReading.toSyncItem() = CounterReadingSyncItem(
            id = id,
            user = AppSettings.user,
            counterId = counterId,
            reading = reading,
            readingTime = readingTime.toEpochMilli(),
            comment = comment
        )
    }


}