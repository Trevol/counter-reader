package com.tavrida.energysales.data_access.dbmodel.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.`java-time`.date
import org.jetbrains.exposed.sql.`java-time`.datetime


open class SimpleIdTable(name: String = "", columnName: String = "id") : IdTable<Int>(name) {
    override val id: Column<EntityID<Int>> = integer(columnName).entityId()
    override val primaryKey by lazy { super.primaryKey ?: PrimaryKey(id) }
}

object ConsumersTable : SimpleIdTable("PUBLIC.CONSUMER") {
    val name = varchar("name", 256).uniqueIndex()
    val comment = varchar("comment", 2000).nullable()
    val importOrder = integer("import_order").uniqueIndex()
}

object CountersTable : SimpleIdTable("PUBLIC.COUNTER") {
    val serialNumber = varchar("serial_number", 20).uniqueIndex()
    val consumerId = reference("consumer_id", ConsumersTable.id)
    val K = integer("K")
    val comment = varchar("comment", 2000).nullable()
    val serializedConsumptionHistory = varchar("serialized_consumption_history", 8000)
    val importOrder = integer("import_order").uniqueIndex()
}

object PrevCounterReadingsTable : SimpleIdTable("PUBLIC.PREV_COUNTER_READING") {
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
    val readingTime = datetime("reading_time")
    val user = varchar("user", 256)
    val comment = varchar("comment", 2000).nullable()
    val synchronized = bool("synchronized")
    val syncTime = datetime("syncTime").nullable()
    val serverId = integer("server_id").nullable()
}

val allTables =
    arrayOf(ConsumersTable, CountersTable, CounterReadingsTable, PrevCounterReadingsTable)
