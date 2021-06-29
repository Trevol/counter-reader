package com.tavrida.energysales.ui.view_models

import androidx.compose.runtime.*
import com.tavrida.energysales.AppSettings
import com.tavrida.energysales.data_access.DatabaseInstance
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

class CounterReadingViewModel(
    val appSettings: AppSettings,
    private val dataContext: IDataContext,
    val dbInstance: DatabaseInstance
) {
    private val scope = CoroutineScope(EmptyCoroutineContext)
    var busy by mutableStateOf(false)
    val search = SearchState(scope) {
        searchConsumers()
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
            val newReading =
                CounterReading(
                    -1,
                    counter.id,
                    newReadingValue,
                    LocalDateTime.now(),
                    null,
                    false,
                    null,
                    null
                )
            counter.readings.add(newReading)
            dataContext.createReading(newReading)
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

    suspend fun uploadReadingsToServer() {
        CounterReadingsSynchronizer(appSettings.backendUrl, appSettings.user, dataContext).uploadReadingsToServer(allConsumers)
    }

    suspend fun reloadCountersFromServer() {

    }


    fun doneAndAllProgress(): String {
        val counters = allConsumers.flatMap { it.counters }
        val doneCount = counters.count { it.recentReading != null }
        return "$doneCount/${counters.size}"
    }

    fun numOfPendingItems(): Int {
        return allConsumers
            .flatMap { it.counters }
            .flatMap { it.readings }
            .count { !it.synchronized }
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