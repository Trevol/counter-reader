package com.tavrida.energysales.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tavrida.energysales.EditableAppSettings
import com.tavrida.energysales.apiClient.CounterReadingSyncApiClient
import com.tavrida.utils.IconButton
import com.tavrida.utils.info
import com.tavrida.utils.rememberMutableStateOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.tavrida.utils.error

@Composable
fun AppSettingsEditorDialog(appSettings: EditableAppSettings, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var checkingServer by rememberMutableStateOf(false)

    fun checkServer() {
        if (appSettings.backendUrl.isNullOrEmpty() || checkingServer) {
            return
        }
        scope.launch {
            CounterReadingSyncApiClient(appSettings.backendUrl!!).use {
                checkingServer = true
                try {
                    val h = it.hello()
                    h.hello
                    delay(500)
                    //info(context, "Проверка связи: Ok")
                    context.toast("Проверка связи: Ok")
                } catch (e: Exception) {
                    delay(500)
                    // error(context, e.message.orEmpty())
                    context.toast("Проверка связи: Error. ${e.message}")
                }
                checkingServer = false
            }
        }
    }

    AlertDialog(
        title = {
            Text("Настройки")
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Сервер") },
                    value = appSettings.backendUrl.orEmpty(),
                    onValueChange = { appSettings.backendUrl = it },
                    enabled = !checkingServer,
                    trailingIcon = {
                        if (checkingServer) {
                            CircularProgressIndicator(strokeWidth = 2.dp)
                        } else {
                            IconButton(Icons.Outlined.FactCheck, onClick = ::checkServer)
                        }
                    }
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Пользователь") },
                    value = appSettings.user.orEmpty(),
                    onValueChange = { appSettings.user = it })

            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            IconButton(Icons.Outlined.Done,
                enabled = !checkingServer,
                onClick = {
                    appSettings.apply()
                    onDismiss()
                }
            )
        }
    )
}

fun Context.toast(text: String){
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}