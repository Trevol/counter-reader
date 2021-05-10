package com.tavrida.energysales

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.tavrida.energysales.data_access.DatabaseInstance
import com.tavrida.energysales.data_access.models.DataContext
import com.tavrida.energysales.ui.components.App
import com.tavrida.energysales.ui.view_models.CounterReadingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: CounterReadingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = CounterReadingViewModel(
            DataContext(DatabaseInstance.get(filesDir))
        )
        setContent {
            App(viewModel)
        }

        lifecycleScope.launchWhenCreated {
            withContext(Dispatchers.IO) {
                viewModel.loadData()
            }
        }
    }

    /*override fun onBackPressed() {
        super.onBackPressed()
    }

    fun getRotation(): Pair<Int, Int> {
        val rotationIdToDegrees = mapOf(0 to 0, 3 to 90, 1 to 270)
        val rotationId = display!!.rotation
        return rotationId to rotationIdToDegrees[rotationId]!!
        // (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }*/

    companion object {
    }
}