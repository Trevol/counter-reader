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

    private val storage by lazy { AppStorage(this, AppSettings.STORAGE_DIR) }
    private val viewModel by lazy {
        val dbInstance  = DatabaseInstance(storage.root)
        CounterReadingViewModel(
            DataContext(dbInstance.db),
            dbInstance
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
        @Composable
        fun NoPermissions() = Text(text = "No permissions!")
    }
}