package com.tavrida.energysales.data_access.dbmodel.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.CurrentTimestamp
import org.jetbrains.exposed.sql.`java-time`.date
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.`java-time`.timestamp

object ConsumersTable : IntIdTable("PUBLIC.CONSUMER") {
    val name = varchar("name", 256).uniqueIndex()
    val comment = varchar("comment", 2000).nullable()
}

object CountersTable : IntIdTable("PUBLIC.COUNTER") {
    val serialNumber = integer("serial_number").uniqueIndex()
    val consumerId = reference("consumer_id", ConsumersTable.id)
    val K = double("K")
    val comment = varchar("comment", 2000).nullable()
}

object PrevCounterReadingsTable : IntIdTable("PUBLIC.PREV_COUNTER_READING") {
    val counterId = reference("counter_id", CountersTable.id)
        .uniqueIndex()
    val reading = double("reading")
    val consumption = double("consumption")
    val readDate = date("read_date")
    val comment = varchar("comment", 2000).nullable()
}

object CounterReadingsTable : IntIdTable("PUBLIC.COUNTER_READING") {
    val counterId = reference("counter_id", CountersTable.id)
    val reading = double("reading")
    val readTime = datetime("read_time").defaultExpression(CurrentTimestamp())
    val comment = varchar("comment", 2000).nullable()
}

val allTables = arrayOf(ConsumersTable, CountersTable, CounterReadingsTable, PrevCounterReadingsTable)
