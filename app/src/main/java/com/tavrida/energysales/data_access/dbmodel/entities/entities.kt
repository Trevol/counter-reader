package com.tavrida.energysales.data_access.dbmodel.entities

/*
import com.tavrida.energysales.data_access.dbmodel.tables.ConsumersTable
import com.tavrida.energysales.data_access.dbmodel.tables.CounterReadingsTable
import com.tavrida.energysales.data_access.dbmodel.tables.CountersTable
import com.tavrida.energysales.data_access.dbmodel.tables.PrevCounterReadingsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Consumer(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Consumer>(ConsumersTable)

    var name by ConsumersTable.name
    var comment by ConsumersTable.comment

    val counters by Counter referrersOn CountersTable.consumerId
}

class Counter(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Counter>(CountersTable)

    var serialNumber by CountersTable.serialNumber
    var consumer by Consumer referencedOn CountersTable.consumerId
    var K by CountersTable.K
    var comment by CountersTable.comment
}

class PrevCounterReading(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PrevCounterReading>(PrevCounterReadingsTable)

    var counter by Counter referencedOn PrevCounterReadingsTable.counterId
    var reading by PrevCounterReadingsTable.reading

    var consumption by PrevCounterReadingsTable.consumption
    var readDate by PrevCounterReadingsTable.readDate
    var comment by PrevCounterReadingsTable.comment
}

class CounterReading(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CounterReading>(CounterReadingsTable)

    var counter by Counter referencedOn CounterReadingsTable.counterId
    var reading by CounterReadingsTable.reading
    var readTime by CounterReadingsTable.readTime
    var comment by CounterReadingsTable.comment
}*/
