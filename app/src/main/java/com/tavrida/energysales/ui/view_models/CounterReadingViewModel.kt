package com.tavrida.energysales.ui.view_models

import androidx.compose.runtime.*
import com.tavrida.energysales.data_access.models.Consumer
import com.tavrida.energysales.data_access.models.DataContext
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

    protected var allCustomers = listOf<Consumer>()
    var visibleCustomers by mutableStateOf(listOf<Consumer>())
    abstract fun loadData()

    fun searchCustomers(query: String) {
        if (query == "")
            visibleCustomers = allCustomers
        else {
            visibleCustomers = allCustomers.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.counters.any {
                            it.serialNumber.toString().contains(query, ignoreCase = true)
                        }
            }
        }
    }
}

class CounterReadingViewModelImpl(val db: Database) : CounterReadingViewModel() {
    override var busy by mutableStateOf(false)
    val dataContext = DataContext(db)

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

    override fun loadData() {
        busy = true
        try {
            allCustomers = dataContext.loadAll()
            searchCustomers("")
        } finally {
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