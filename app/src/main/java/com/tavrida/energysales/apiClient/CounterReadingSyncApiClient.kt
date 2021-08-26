package com.tavrida.energysales.apiClient

import com.tavrida.energysales.data_contract.OrganizationData
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import com.tavrida.energysales.data_contract.CounterReadingIdMapping
import com.tavrida.energysales.data_contract.CounterReadingItem
import com.tavrida.energysales.data_contract.HelloResponse
import com.tavrida.utils.ensureTrailingChar
import io.ktor.client.features.json.serializer.*

class CounterReadingSyncApiClient(
    serverUrl: String
) : AutoCloseable {
    private val serverUrl = ensureTrailingChar(serverUrl, pathDelimiter)

    private fun endpointUrl(path: String) = "$serverUrl$path"

    private val httpClient = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    override fun close() {
        httpClient.close()
    }


    private suspend inline fun <R : Any, reified T> postJson(endpointPath: String, data: R): T {
        return httpClient.post(urlString = endpointUrl(endpointPath)) {
            body = data
            contentType(ContentType.Application.Json)
        }
    }

    private suspend inline fun <reified T> getJson(endpointPath: String): T {
        return httpClient.get(urlString = endpointUrl(endpointPath))
    }


    suspend fun hello(): HelloResponse {
        return getJson("api/hello")
    }

    suspend fun uploadMobileReadings(items: List<CounterReadingItem>): List<CounterReadingIdMapping> {
        return postJson("/api/mobile/readings", items)
    }

    suspend fun getRecentData(): List<OrganizationData> {
        return getJson("/api/mobile/recent_data")
    }
}

private const val pathDelimiter = '/'
