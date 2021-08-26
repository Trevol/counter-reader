package com.tavrida.energysales.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.tavrida.energysales.data_access.models.Consumer
import com.tavrida.energysales.data_access.models.CounterReading
import com.tavrida.energysales.data_access.models.IDataContext
import com.tavrida.energysales.ui.view_models.CounterReadingViewModel

/*
@Preview(showBackground = true)
@Composable
fun AppPreview() {
    val viewModel = remember { CounterReadingViewModel(DummyDataContext(), dbInstance) }
    App(viewModel)
}

private class DummyDataContext : IDataContext {
    override fun loadAll(): List<Consumer> {
        return listOf()
    }

    override fun updateReading(reading: CounterReading) {

    }

    override fun createReading(newReading: CounterReading) {

    }

    override fun updateSyncData(unsynchronized: List<CounterReading>) {

    }
}*/
