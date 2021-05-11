package com.tavrida.energysales.ui.view_models

import androidx.compose.runtime.*
import com.tavrida.energysales.data_access.models.Consumer
import com.tavrida.energysales.data_access.models.Counter
import com.tavrida.energysales.data_access.models.CounterReading
import com.tavrida.energysales.data_access.models.IDataContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class IndexedConsumer(val consumer: Consumer, val index: Int)

class ConsumerDetailsState(val consumer: IndexedConsumer, showDetails: Boolean) {
    class SelectedCounterState(val counter: Counter, showReadingEditor: Boolean) {
        var showReadingEditor by mutableStateOf(showReadingEditor)
    }

    var showDetails by mutableStateOf(showDetails)
    var selectedCounter by mutableStateOf(null as SelectedCounterState?)

    fun selectCounter(counter: Counter, showReadingEditor: Boolean = false) {
        selectedCounter = SelectedCounterState(counter, showReadingEditor = showReadingEditor)
    }
}

class CounterReadingViewModel(val dataContext: IDataContext) {
    var busy by mutableStateOf(false)

    protected var allConsumers = listOf<Consumer>()
    var visibleConsumers by mutableStateOf(listOf<Consumer>())
    fun loadData() {
        busy = true
        try {
            allConsumers = dataContext.loadAll()
            searchCustomers("")
        } finally {
            busy = false
        }
    }

    var selectedConsumer by mutableStateOf(null as ConsumerDetailsState?)
    fun selectConsumer(consumer: IndexedConsumer, showDetails: Boolean) {
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

    fun activateCounterBySerialNumber(sn: String): Boolean {
        clearSelection()
        if (sn.isEmpty()) {
            return false
        }

        val found = allConsumers.findBySn(sn) ?: return false

        selectedConsumer = ConsumerDetailsState(found.consumer, showDetails = true)
            .apply {
                selectCounter(found.counter, showReadingEditor = true)
            }

        return true
    }

    fun applyNewReading(counter: Counter, newReadingValue: Double) {
        busy {
            val reading = counter.currentReading
            if (reading != null) {
                reading.reading = newReadingValue
                reading.readTime = LocalDateTime.now()

                dataContext.updateReading(reading)
            } else {
                val newReading =
                    CounterReading(-1, counter.id, newReadingValue, LocalDateTime.now(), null)
                counter.readings.add(newReading)
                dataContext.createReading(newReading)
            }
        }
    }

    private fun busy(block: () -> Unit) {
        busy = true
        GlobalScope.launch {
            try {
                block()
            } finally {
                busy = false
            }
        }
    }

    companion object {
        private data class IndexedConsumerWithCounter(
            val consumer: IndexedConsumer,
            val counter: Counter
        )

        private fun List<Consumer>.findBySn(sn: String): IndexedConsumerWithCounter? {
            forEachIndexed { consumerIndex, consumer ->
                val counter = consumer.counters.firstOrNull { it.serialNumber == sn }
                if (counter != null) {
                    return IndexedConsumerWithCounter(
                        IndexedConsumer(consumer, consumerIndex),
                        counter
                    )
                }
            }
            return null
        }
    }
}

