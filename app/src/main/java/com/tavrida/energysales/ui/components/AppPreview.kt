package com.tavrida.energysales.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.tavrida.energysales.ui.view_models.CounterReadingViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    val viewModel = remember { DummyViewModel() }
    App(viewModel)
}

private class DummyViewModel : CounterReadingViewModel() {
    override var busy: Boolean = false
    override fun save() {
        busy = true
        GlobalScope.launch {
            delay(3000)
            busy = false
        }
    }

    override fun loadData() {
    }
}