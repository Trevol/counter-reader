package com.tavrida.energysales.ui.view_models

import androidx.compose.runtime.mutableStateListOf
import com.tavrida.energysales.apiClient.CounterReadingSyncApiClient
import com.tavrida.energysales.data_access.models.*
import com.tavrida.energysales.data_contract.ConsumerData
import com.tavrida.energysales.data_contract.CounterData
import com.tavrida.energysales.data_contract.CounterReadingItem
import com.tavrida.energysales.data_contract.PrevCounterReadingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class CounterReadingsSynchronizer(
    val backendUrl: String?,
    val dataContext: IDataContext
) {
    init {
        if (backendUrl.isNullOrEmpty()) {
            throw Exception("Backend URL is empty")
        }
    }

    suspend fun uploadReadingsToServer(allConsumers: List<Consumer>) {
        withContext(Dispatchers.IO) {
            val unsynchronized = allConsumers.unsynchronizedReadings()
            if (unsynchronized.isEmpty()) {
                return@withContext
            }

            val items = unsynchronized.map { it.toUploadItem() }
            val idMappings = apiClient(backendUrl!!).use {
                it.uploadMobileReadings(items)
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

    suspend fun reloadRecentDataFromServer(): Boolean {
        return withContext(Dispatchers.IO) {
            val consumerData = apiClient(backendUrl!!).use {
                it.getRecentData()
            }
            if (consumerData.isEmpty()) {
                return@withContext false
            }

            dataContext.recreateDb()
            dataContext.insertAll(consumerData.map { it.toConsumer() })
            true
        }
    }

    companion object {
        fun apiClient(backendUrl: String) =
            CounterReadingSyncApiClient(backendUrl)

        fun Iterable<Consumer>.unsynchronizedReadings() =
            flatMap { it.counters }.flatMap { it.readings }
                .filter { !it.synchronized }

        fun CounterReading.toUploadItem() = CounterReadingItem(
            id = id,
            user = user,
            counterId = counterId,
            reading = reading,
            readingTime = readingTime,
            comment = comment
        )

        private fun ConsumerData.toConsumer(): Consumer {
            return Consumer(
                id = id,
                name = name,
                counters = counters.map { it.toCounter() }.toMutableList(),
                comment = comment,
                importOrder = importOrder
            )
        }

        private fun CounterData.toCounter(): Counter {
            return Counter(
                id = id,
                serialNumber = serialNumber,
                consumerId = consumerId,
                K = K,
                prevReading = prevReading.toPrevReading(),
                readings = mutableStateListOf(),
                comment = comment,
                importOrder = importOrder
            )
        }

        private fun PrevCounterReadingData.toPrevReading(): PrevCounterReading {
            return PrevCounterReading(
                id = id,
                counterId = counterId,
                reading = reading,
                consumption = consumption,
                readDate = readDate,
                comment = comment
            )
        }
    }


}