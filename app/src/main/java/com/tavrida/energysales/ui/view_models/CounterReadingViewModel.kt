package com.tavrida.energysales.ui.view_models

import androidx.compose.runtime.*
import com.tavrida.energysales.data_access.models.Consumer
import com.tavrida.energysales.data_access.models.Counter
import com.tavrida.energysales.data_access.models.CounterReading
import com.tavrida.energysales.data_access.models.IDataContext
import kotlinx.coroutines.*
import java.time.LocalDateTime
import kotlin.coroutines.EmptyCoroutineContext

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

class SearchState(private val scope: CoroutineScope, val searchAction: () -> Unit) {
    var query by mutableStateOf("")
        private set

    @JvmName("setQuery2")
    fun setQuery(value: String, immediateAction: Boolean = false) {
        query = value
        deferSearchAction?.cancel()
        if (immediateAction) {
            searchAction()
        } else {
            deferSearchAction = scope.launch {
                delay(searchDebounce)
                searching = true
                searchAction()
                searching = false
            }
        }
    }

    var searching by mutableStateOf(false)
        private set

    //for debouncing value stream (from keyboard)
    private val searchDebounce: Long = 500
    private var deferSearchAction = null as Job?
}

class CounterReadingViewModel(private val dataContext: IDataContext) {
    private val scope = CoroutineScope(EmptyCoroutineContext)
    var busy by mutableStateOf(false)
    val search = SearchState(scope) {
        busy {
            searchConsumers()
        }
    }
    private var allConsumers = listOf<Consumer>()
    var visibleConsumers by mutableStateOf(listOf<Consumer>())
    suspend fun loadData() {
        busy {
            allConsumers = dataContext.loadAll()
            searchConsumers()
        }
    }

    var selectedConsumer by mutableStateOf(null as ConsumerDetailsState?)
    fun selectConsumer(consumer: IndexedConsumer, showDetails: Boolean) {
        selectedConsumer = ConsumerDetailsState(consumer, showDetails = showDetails)
    }

    private fun clearSelection() {
        selectedConsumer = null
    }

    private fun searchConsumers() {
        val query = search.query
        if (query.isEmpty()) {
            visibleConsumers = allConsumers
            if (selectedConsumer != null) //preserve selection on clearing filter
            {
                val consumer = selectedConsumer!!.consumer.consumer
                val indexedConsumer = IndexedConsumer(consumer, visibleConsumers.indexOf(consumer))
                selectedConsumer = ConsumerDetailsState(indexedConsumer, showDetails = false)
            }
        } else {
            clearSelection()
            visibleConsumers = allConsumers.filter(query)
        }
    }

    fun activateCounterBySerialNumber(sn: String): Boolean {
        clearSelection()
        if (sn.isEmpty()) {
            return false
        }
        search.setQuery("", immediateAction = true)
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

    private fun busy(block: suspend () -> Unit) {
        busy = true
        scope.launch {
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

        private fun List<Consumer>.filter(query: String) = filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.counters.any {
                        it.serialNumber.contains(query, ignoreCase = true) ||
                                it.comment?.contains(query, ignoreCase = true) == true
                    }
        }

    }
}

