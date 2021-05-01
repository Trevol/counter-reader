package com.tavrida.energysales.ui.view_models

import androidx.compose.runtime.*
import com.tavrida.energysales.data_access.models.Consumer
import com.tavrida.energysales.data_access.models.Counter
import com.tavrida.energysales.data_access.models.DataContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.Database


abstract class CounterReadingViewModel {
    abstract var busy: Boolean
        protected set

    abstract fun save()
    fun canSave(): Boolean {
        return !busy
    }

    protected var allConsumers = listOf<Consumer>()
    var visibleConsumers by mutableStateOf(listOf<Consumer>())
    abstract fun loadData()

    var selectedConsumer by mutableStateOf<Consumer?>(null)
    var activeCounter by mutableStateOf<Counter?>(null)

    fun clearSelection() {
        selectedConsumer = null
        activeCounter = null
    }

    fun searchCustomers(query: String) {
        if (query == "")
            visibleConsumers = allConsumers
        else {
            visibleConsumers = allConsumers.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.counters.any {
                            it.serialNumber.toString().contains(query, ignoreCase = true)
                        }
            }
        }
    }

    fun activateCounterBySerialNumber(sn: Int): Boolean {
        clearSelection()
        if (sn <= 0) {
            return false
        }
        for (consumer in allConsumers) {
            for (counter in consumer.counters) {
                if (counter.serialNumber == sn) {
                    selectedConsumer = consumer
                    activeCounter = counter
                    return true
                }
            }
        }
        return false
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
            busy = false
        }
    }

    override fun loadData() {
        busy = true
        try {
            allConsumers = dataContext.loadAll()
            searchCustomers("")
        } finally {
            busy = false
        }
    }
}