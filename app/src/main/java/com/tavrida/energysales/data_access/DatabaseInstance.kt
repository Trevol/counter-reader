package com.tavrida.energysales.data_access

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

object DatabaseInstance {
    const val DB_NAME = "ENERGY_SALES_MOBILE"


    fun get(directory: File) = dbUrl(directory).let { Database.connect(it).initSchema() }

    private fun dbUrl(directory: File) =
        File(directory, DB_NAME)
            .let { dbPath ->
                "jdbc:h2:${dbPath.absolutePath}"
            }

    private var schemaInitialized = false
    private fun Database.initSchema(): Database {
        if (schemaInitialized) {
            return this
        }
        transaction(this) {
            SchemaUtils.create(*Schema.allTables)
        }
        schemaInitialized = true
        return this
    }

}