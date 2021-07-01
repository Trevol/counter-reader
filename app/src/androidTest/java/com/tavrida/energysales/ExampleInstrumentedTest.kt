package com.tavrida.energysales

import androidx.compose.runtime.toMutableStateList
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tavrida.energysales.apiClient.CounterReadingSyncApiClient
import com.tavrida.energysales.data_access.dbmodel.tables.allTables
import com.tavrida.energysales.data_access.models.*
import com.tavrida.utils.padStartEx
import org.jetbrains.exposed.sql.transactions.transaction
import com.tavrida.utils.println
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*

import org.junit.Test
import org.junit.runner.RunWith

import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.system.measureTimeMillis

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun generateData() {
        runBlocking {
            CounterReadingSyncApiClient("http://192.168.0.112:8080").use { it.getRecentData() }
        }
    }


    companion object {
        fun appContext() = InstrumentationRegistry.getInstrumentation().targetContext
    }
}