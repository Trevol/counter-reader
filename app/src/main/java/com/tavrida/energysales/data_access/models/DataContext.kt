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
                            it[readTime] = reading.readTime
                            it[comment] = reading.comment
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
            .orderBy(ConsumersTable.name)
            .toList()
        if (consumerRows.isEmpty()) {
            listOf()
        } else {
            val counterRows = CountersTable.selectAll()
                .orderBy(CountersTable.serialNumber)
                .toList()
            val readingRows = CounterReadingsTable.selectAll().toList()
            val prevReadingRows = PrevCounterReadingsTable.selectAll().toList()
            connectConsumerEntities(consumerRows, counterRows, readingRows, prevReadingRows)
        }
    }

    override fun updateReading(reading: CounterReading) {
        transaction(db) {
            CounterReadingsTable.update({ CounterReadingsTable.id eq reading.id }) {
                it[this.reading] = reading.reading
                it[readTime] = reading.readTime
                it[comment] = reading.comment
            }
        }
    }

    override fun createReading(newReading: CounterReading) {
        transaction(db) {
            val id = CounterReadingsTable.insertAndGetId {
                it[counterId] = newReading.counterId
                it[reading] = newReading.reading
                it[readTime] = newReading.readTime
                it[comment] = newReading.comment
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
                readTime = it[t.readTime],
                comment = it[t.comment]
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