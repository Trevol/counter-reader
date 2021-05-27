package com.tavrida.energysales.apiClient

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import com.tavrida.energysales.data_contract.CounterReadingIdMapping
import com.tavrida.energysales.data_contract.CounterReadingSyncItem
import com.tavrida.energysales.data_contract.CounterReadingSyncRequest

class CounterReadingSyncApiClient(
    private val serverHost: String,
    private val serverPort: Int,
) : AutoCloseable {

    private val httpClient = HttpClient(CIO) {
        install(JsonFeature)
    }

    override fun close() {
        httpClient.close()
    }

    suspend fun sync(items: List<CounterReadingSyncItem>, testMode: Boolean): List<CounterReadingIdMapping> =
        httpClient.post(
            host = serverHost,
            port = serverPort,
            path = "api/syncReadings",
            body = CounterReadingSyncRequest(items, testMode)
        ) {
            contentType(ContentType.Application.Json)
        }
}