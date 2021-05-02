package com.tavrida.energysales.ui.view_models

import androidx.compose.runtime.*
import com.tavrida.energysales.data_access.models.Consumer
import com.tavrida.energysales.data_access.models.Counter

class ConsumerDetailsState(val consumer: Consumer, showDetails: Boolean) {
    class SelectedCounterState(val counter: Counter, showReadingEditor: Boolean) {
        var showReadingEditor by mutableStateOf(showReadingEditor)
    }

    var showDetails by mutableStateOf(showDetails)
    var selectedCounter by mutableStateOf(null as SelectedCounterState?)

    fun selectCounter(counter: Counter, showReadingEditor: Boolean = false) {
        selectedCounter = SelectedCounterState(counter, showReadingEditor = showReadingEditor)
    }
}

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

    var selectedConsumer by mutableStateOf(null as ConsumerDetailsState?)
    fun selectConsumer(consumer: Consumer, showDetails: Boolean) {
        selectedConsumer = ConsumerDetailsState(consumer, showDetails = showDetails)
    }
    fun clearSelection() {
        selectedConsumer = null
    }

    fun searchCustomers(query: String) {
        clearSelection()
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
                    selectedConsumer = ConsumerDetailsState(consumer, showDetails = true)
                        .apply {
                            selectCounter(counter, showReadingEditor = true)
                        }
                    return true
                }
            }
        }
        return false
    }
}

