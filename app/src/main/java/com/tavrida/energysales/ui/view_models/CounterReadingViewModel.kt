package com.tavrida.energysales.ui.view_models

import androidx.compose.runtime.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.Database


abstract class CounterReadingViewModel {
    abstract var busy: Boolean
        protected set
    val counterReading = CounterReadingInputState()

    abstract fun save()
    fun canSave(): Boolean {
        return !busy && counterReading.isValid
    }
}

class CounterReadingViewModelImpl(val db: Database) : CounterReadingViewModel() {
    override var busy by mutableStateOf(false)

    override fun save() {
        if (!canSave()) {
            return
        }
        busy = true
        GlobalScope.launch {
            saveReading()
            counterReading.clear()
            busy = false
        }
    }

    private fun saveReading() {
        TODO()
        /*transaction(db) {
            Tables.DummyCounterReading.insert {
                it[counterId] = counterReading.counterId!!
                it[reading] = counterReading.reading!!
            }
        }*/
    }
}