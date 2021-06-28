package com.tavrida.energysales.data_access

import com.tavrida.energysales.data_access.dbmodel.tables.allTables
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

class DatabaseInstance(private val dataDir: File) {
    private var _db: Database? = null

    val db
        get() = _db ?: initDb(dataDir).also { _db = it }


    fun recreate() {
        deleteDbFiles(dataDir)
        _db = initDb(dataDir)
    }

    companion object {
        const val DB_NAME = "ENERGY_SALES_MOBILE"

        private fun dbUrl(dataDir: File) =
            File(dataDir, DB_NAME)
                .let { dbPath ->
                    "jdbc:h2:${dbPath.absolutePath}"
                }

        private fun dbMvFile(dataDir: File) = File(dataDir, "$DB_NAME.mv.db")
        private fun dbTraceFile(dataDir: File) = File(dataDir, "$DB_NAME.trace.db")
        private fun dbFileExists(dataDir: File) = dbMvFile(dataDir).isFile
        private fun deleteDbFiles(dataDir: File) {
            dbMvFile(dataDir).delete()
            dbTraceFile(dataDir).delete()
        }

        private fun Database.initSchema(): Database {
            transaction(this) {
                SchemaUtils.create(*allTables)
            }
            return this
        }

        private fun initDb(dataDir: File) = if (dbFileExists(dataDir)) {
            Database.connect(dbUrl(dataDir))
        } else {
            Database.connect(dbUrl(dataDir)).initSchema()
        }
    }
}