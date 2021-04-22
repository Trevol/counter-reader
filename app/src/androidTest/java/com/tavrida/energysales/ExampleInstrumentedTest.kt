package com.tavrida.energysales

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tavrida.energysales.data_access.DatabaseInstance
import com.tavrida.utils.println

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.io.FilenameFilter

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun deleteDatabase() {
        val dbName = DatabaseInstance.DB_NAME
        val dbFiles = appContext().filesDir.listFiles { dir, name -> name.startsWith(dbName) }
        for (file in dbFiles){
            file.absolutePath.println()
            file.delete()
        }
    }


    companion object {
        fun appContext() = InstrumentationRegistry.getInstrumentation().targetContext
    }
}