package com.tavrida.energysales

import com.tavrida.energysales.apiClient.CounterReadingSyncApiClient
import io.ktor.http.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        runBlocking {
            CounterReadingSyncApiClient("http://192.168.0.112:8080").use { it.getRecentData() }
        }
    }
}