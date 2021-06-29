package com.tavrida.energysales.apiClient

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import com.tavrida.energysales.data_contract.CounterReadingIdMapping
import com.tavrida.energysales.data_contract.CounterReadingSyncItem
import com.tavrida.energysales.data_contract.CounterReadingSyncRequest
import com.tavrida.utils.ensureTrailingChar

class CounterReadingSyncApiClient(
    serverUrl: String
) : AutoCloseable {
    val serverUrl = ensureTrailingChar(serverUrl, pathDelimiter)

    private fun endpointUrl(path: String) = "$serverUrl$path"

    private val httpClient = HttpClient(CIO) {
        install(JsonFeature)
    }

    override fun close() {
        httpClient.close()
    }

    private suspend inline fun <R : Any, reified T> postJson(endpointPath: String, data: R): T {
        return httpClient.post(
            urlString = endpointUrl(endpointPath)
        ) {
            this.body = data
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun sync(items: List<CounterReadingSyncItem>): List<CounterReadingIdMapping> {
        return postJson("api/syncReadings", CounterReadingSyncRequest(items))
        /*return httpClient.post(
            host = serverHost,
            port = serverPort,
            path = "api/syncReadings",
            body = CounterReadingSyncRequest(items)
        ) {
            contentType(ContentType.Application.Json)
        }*/
    }
}

private const val pathDelimiter = '/'
