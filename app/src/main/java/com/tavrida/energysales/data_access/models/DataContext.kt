package com.tavrida.energysales.data_access.models

import androidx.compose.runtime.toMutableStateList
import com.tavrida.energysales.data_access.dbmodel.tables.ConsumersTable
import com.tavrida.energysales.data_access.dbmodel.tables.CounterReadingsTable
import com.tavrida.energysales.data_access.dbmodel.tables.CountersTable
import com.tavrida.energysales.data_access.dbmodel.tables.PrevCounterReadingsTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

interface IDataContext {
    fun loadAll(): List<Consumer>
    fun updateReading(reading: CounterReading)
    fun createReading(newReading: CounterReading)
    fun updateSyncData(unsynchronized: List<CounterReading>)
}

class DataContext(val db: Database) : IDataContext {
    fun insertAll(consumers: List<Consumer>) {
        if (consumers.isEmpty()) {
            return
        }
        transaction(db) {
            for (consumer in consumers) {
                val consId = ConsumersTable.insertAndGetId {
                    it[name] = consumer.name
                    it[comment] = consumer.comment
                    it[importOrder] = consumer.importOrder
                }.value

                for (counter in consumer.counters) {
                    val counterId = CountersTable.insertAndGetId {
                        it[serialNumber] = counter.serialNumber
                        it[consumerId] = consId
                        it[K] = counter.K
                        it[comment] = counter.comment
                        it[importOrder] = counter.importOrder
                    }.value

                    for (reading in counter.readings) {
                        CounterReadingsTable.insert {
                            it[this.counterId] = counterId
                            it[this.reading] = reading.reading
                            it[readingTime] = reading.readingTime
                            it[comment] = reading.comment
                            it[synchronized] = reading.synchronized
                            it[syncTime] = reading.syncTime
                            it[serverId] = reading.serverId
                        }
                    }

                    PrevCounterReadingsTable.insert {
                        it[this.counterId] = counterId
                        it[reading] = counter.prevReading.reading
                        it[consumption] = counter.prevReading.consumption
                        it[readDate] = counter.prevReading.readDate
                        it[comment] = counter.prevReading.comment
                    }
                }
            }
        }
    }

    override fun loadAll() = transaction(db) {
        val consumerRows = ConsumersTable.selectAll()
            .orderBy(ConsumersTable.importOrder)
            .toList()
        if (consumerRows.isEmpty()) {
            listOf()
        } else {
            val counterRows = CountersTable.selectAll()
                .orderBy(CountersTable.importOrder)
                .toList()
            val readingRows = CounterReadingsTable.selectAll().toList()
            val prevReadingRows = PrevCounterReadingsTable.selectAll().toList()
            connectConsumerEntities(consumerRows, counterRows, readingRows, prevReadingRows)
        }
    }

    override fun updateSyncData(unsynchronized: List<CounterReading>) {
        if (unsynchronized.isEmpty()) {
            return
        }
        transaction(db) {
            for (r in unsynchronized) {
                CounterReadingsTable.update({ CounterReadingsTable.id eq r.id }) {
                    it[synchronized] = r.synchronized
                    it[syncTime] = r.syncTime
                    it[serverId] = r.serverId
                }
            }
        }
    }

    override fun updateReading(reading: CounterReading) {
        transaction(db) {
            CounterReadingsTable.update({ CounterReadingsTable.id eq reading.id }) {
                it[this.reading] = reading.reading
                it[readingTime] = reading.readingTime
                it[comment] = reading.comment
                it[user] = reading.user
                it[synchronized] = reading.synchronized
                it[syncTime] = reading.syncTime
                it[serverId] = reading.serverId
            }
        }
    }

    override fun createReading(newReading: CounterReading) {
        transaction(db) {
            val id = CounterReadingsTable.insertAndGetId {
                it[counterId] = newReading.counterId
                it[reading] = newReading.reading
                it[readingTime] = newReading.readingTime
                it[user] = newReading.user
                it[comment] = newReading.comment
                it[synchronized] = newReading.synchronized
                it[syncTime] = newReading.syncTime
                it[serverId] = newReading.serverId
            }
            newReading.id = id.value
        }
    }

    private fun connectConsumerEntities(
        consumerRows: List<ResultRow>,
        countersRows: List<ResultRow>,
        readingRows: List<ResultRow>,
        prevReadingRows: List<ResultRow>
    ): List<Consumer> {
        if (consumerRows.isEmpty())
            return listOf()

        val prevReadingByCounterId = prevReadingRows.associateBy(
            { it[PrevCounterReadingsTable.counterId].value },
            {
                val t = PrevCounterReadingsTable
                PrevCounterReading(
                    id = it[t.id].value,
                    counterId = it[t.counterId].value,
                    reading = it[t.reading],
                    consumption = it[t.consumption],
                    readDate = it[t.readDate],
                    comment = it[t.comment]
                )
            }
        )

        val readings = readingRows.map {
            val t = CounterReadingsTable
            CounterReading(
                id = it[t.id].value,
                counterId = it[t.counterId].value,
                reading = it[t.reading],
                readingTime = it[t.readingTime],
                user = it[t.user],
                comment = it[t.comment],
                synchronized = it[t.synchronized],
                syncTime = it[t.syncTime],
                serverId = it[t.serverId],
            )
        }

        val counters = countersRows.map {
            val t = CountersTable
            val counterId = it[t.id].value
            Counter(
                id = counterId,
                serialNumber = it[t.serialNumber],
                consumerId = it[t.consumerId].value,
                K = it[t.K],
                prevReading = prevReadingByCounterId[counterId]!!,
                readings = readings.filter { it.counterId == counterId }.toMutableStateList(),
                comment = it[t.comment],
                importOrder = it[t.importOrder]
            )
        }

        return consumerRows.map {
            val t = ConsumersTable
            val consumerId = it[t.id].value
            Consumer(
                id = consumerId,
                name = it[t.name],
                counters = counters.filter { it.consumerId == consumerId }.toMutableList(),
                comment = it[t.comment],
                importOrder = it[t.importOrder]
            )
        }
    }
}

fun <T> transaction(dc: DataContext, statement: Transaction.() -> T): T =
    transaction(dc.db, statement)