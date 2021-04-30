package com.tavrida.energysales

import androidx.compose.runtime.toMutableStateList
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tavrida.energysales.data_access.dbmodel.tables.allTables
import com.tavrida.energysales.data_access.models.*
import com.tavrida.utils.padStartEx
import org.jetbrains.exposed.sql.transactions.transaction
import com.tavrida.utils.println
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

        val dc = DbInstance(appContext().filesDir, "ENERGY_SALES_MOBILE", allTables.toList())
            .get(recreate = true)
            .let { DataContext(it) }

        val consumers = dummyData(500)
        transaction(dc) {
            dc.insertAll(consumers)
        }

        transaction(dc) {
            measureTimeMillis {
                val allConsumers = dc.loadAll()
            }.println()
            measureTimeMillis {
                val allConsumers = dc.loadAll()
            }.println()
            measureTimeMillis {
                val allConsumers = dc.loadAll()
            }.println()
        }
    }


    companion object {
        fun appContext() = InstrumentationRegistry.getInstrumentation().targetContext
    }
}

private fun dummyData(nOfConsumers: Int): List<Consumer> {
    // нечетный потребитель - 1 счетчик
    // четный потребитель -
    //      2 счетчика - k=1 и k=50

    fun readings(
        consumerPos: Int,
        counterPos: Int,
        serialNumber: Int,
        K: Double
    ): Pair<List<CounterReading>, PrevCounterReading> {
        val monthAgo = LocalDateTime.now().minusMonths(1)
        val twoMonthsAgo = monthAgo.minusMonths(1)
        val readingTwoMonthsAgo = consumerPos * 100.0
        val readingDeltaForMonth = consumerPos * 10.0 + counterPos
        val readingMonthAgo = readingTwoMonthsAgo + readingDeltaForMonth
        val readings = listOf(
            CounterReading(
                id = -1,
                counterId = -1,
                reading = readingTwoMonthsAgo,
                readTime = twoMonthsAgo,
                comment = "Это показания $twoMonthsAgo"
            ),
            CounterReading(
                id = -1,
                counterId = -1,
                reading = readingMonthAgo,
                readTime = monthAgo,
                comment = "Это показания $monthAgo"
            )
        )
        val prevReading = PrevCounterReading(
            id = -1,
            counterId = -1,
            reading = readingMonthAgo,
            consumption = readingDeltaForMonth * K,
            readDate = monthAgo.atZone(ZoneOffset.UTC).toLocalDate(),
            comment = "This is prev reading of $serialNumber"
        )
        return readings to prevReading
    }

    val maxLen = nOfConsumers.toString().length
    return (1..nOfConsumers).map { consumerPos ->
        val consumerName = "Потребитель_${consumerPos.padStartEx(maxLen, '0')}"
        val oneOrTwo = if (consumerPos % 2 == 1) (1..1) else (1..2)
        val counters = oneOrTwo.map { counterPos ->
            val serialNumber = "${consumerPos}0$counterPos".toInt()
            val K = if (counterPos % 2 == 0) 1.0 else 50.0
            val (readings, prevReading) = readings(consumerPos, counterPos, serialNumber, K)
            Counter(
                id = -1,
                serialNumber = serialNumber,
                consumerId = -1,
                K = K,
                prevReading = prevReading,
                readings = readings.toMutableStateList(),
                comment = "Счетчик $serialNumber потребителя $consumerName!!"
            )

        }

        Consumer(
            id = -1,
            name = consumerName,
            comment = "this is consumer №$consumerPos",
            counters = counters
        )
    }
}

private fun loadAll() {
    val dc = DbInstance("./databases/", "ENERGY_SALES_MOBILE", allTables.toList())
        .get(recreate = true)
        .let { DataContext(it) }
    transaction(dc) {
        // addLogger(StdOutSqlLogger)
        dc.loadAll()
    }
}

private class DbInstance(
    val dbDir: File, val dbName: String, val tables: List<Table>
) {
    constructor(
        dbDir: String,
        dbName: String,
        tables: List<Table>
    ) : this(File(dbDir), dbName, tables)

    fun get(recreate: Boolean): Database {
        if (recreate) {
            deleteFileInDir(dbDir, dbName)
            schemaCreated = false
        }
        return connect().ensureSchema(tables)
    }

    private fun connect() = "jdbc:h2:${File(dbDir, dbName)}"
        .let { url ->
            Database.connect(url)
        }

    companion object {
        private var schemaCreated = false
        private fun Database.ensureSchema(tables: List<Table>): Database {
            if (schemaCreated) return this
            transaction(this) {
                SchemaUtils.create(*tables.toTypedArray())
            }
            schemaCreated = true
            return this
        }
    }
}

fun deleteFileInDir(dbDir: String, startsWith: String) {
    deleteFileInDir(File(dbDir), startsWith)
}

fun deleteFileInDir(dbDir: File, startsWith: String) {
    dbDir
        .listFiles { dir, name -> name.startsWith(startsWith, true) }!!
        .forEach { it.delete() }
}