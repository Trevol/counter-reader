package com.tavrida.energysales

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.tavrida.energysales.data_access.DatabaseInstance
import com.tavrida.energysales.data_access.models.DataContext
import com.tavrida.energysales.ui.components.App
import com.tavrida.energysales.ui.theme.CounterReaderTheme
import com.tavrida.energysales.ui.view_models.CounterReadingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random
import com.tavrida.utils.error

class MainActivity : ComponentActivity() {

    private val permissions =
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    private val permissionsRequestCode = Random.nextInt(0, 10000)
    private fun hasPermissions(context: Context) = permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionsRequestCode && hasPermissions(this)) {
            ready()
        }
    }

    private var permissionsGranted by mutableStateOf(false)

    private val appSettings by lazy { AppSettings(getPreferences(MODE_PRIVATE)) }
    private val storage by lazy { AppStorage(this, appSettings.storageDirectory) }
    private val viewModel by lazy {
        val dbInstance = DatabaseInstance(storage.root)
        CounterReadingViewModel(
            appSettings,
            DataContext(dbInstance)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (hasPermissions(this)) {
            ready()
        } else {
            ActivityCompat.requestPermissions(
                this, permissions.toTypedArray(), permissionsRequestCode
            )
        }

        setContent {
            CounterReaderTheme {
                if (permissionsGranted) {
                    App(viewModel)
                } else {
                    NoPermissions()
                }
            }
        }
    }

    private fun ready() {
        permissionsGranted = true
        viewModel // trigger creation...
        lifecycleScope.launchWhenCreated {
            withContext(Dispatchers.IO) {
                try {
                    viewModel.loadLocalData()
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        error(this@MainActivity, "${e.message}")
                    }
                }
            }
        }
    }

    companion object {
        @Composable
        fun NoPermissions() = Text(text = "No permissions!")
    }
}