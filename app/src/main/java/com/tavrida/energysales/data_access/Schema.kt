package com.tavrida.energysales.data_access

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.CurrentTimestamp
import org.jetbrains.exposed.sql.`java-time`.date
import org.jetbrains.exposed.sql.`java-time`.timestamp

object Schema {
    object Consumers : IntIdTable("PUBLIC.CONSUMER") {
        val name = varchar("name", 256).uniqueIndex()
        val comment = varchar("comment", 2024).nullable()
    }

    object Counters : IntIdTable("PUBLIC.COUNTER") {
        val serialNumber = integer("serial_number").uniqueIndex()
        val consumerId = integer("consumer_id").references(Consumers.id)
        val K = integer("K")
        val comment = varchar("comment", 2024).nullable()
    }

    object PrevCounterReadings : IntIdTable("PUBLIC.PREV_COUNTER_READING") {
        val counterId = integer("counter_id")
            .uniqueIndex()
            .references(Counters.id)
        val reading = double("reading")
        val consumption = double("consumption")
        val readDate = date("read_date")
        val comment = varchar("comment", 2024).nullable()
    }

    object CounterReadings : IntIdTable("PUBLIC.COUNTER_READING") {
        val counterId = integer("counter_id")
            .references(Counters.id)
        val reading = double("reading")
        val read_instant = timestamp("read_time").defaultExpression(CurrentTimestamp())
        val comment = varchar("comment", 2024).nullable()
    }

    val allTables = arrayOf(Consumers, Counters, CounterReadings, PrevCounterReadings)
}