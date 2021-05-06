package com.tavrida.energysales.ui.view_models

//OBSOLETE
/*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.tavrida.energysales.data_access.models.DataContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.Database

class CounterReadingViewModelImpl(val db: Database) : CounterReadingViewModel() {
    val dataContext = DataContext(db)

    override fun loadData() {
        busy = true
        try {
            allConsumers = dataContext.loadAll()
            searchCustomers("")
        } finally {
            busy = false
        }
    }
}*/
