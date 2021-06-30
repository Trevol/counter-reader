package com.tavrida.energysales.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
                    it.hello()
                    delay(500)
                    info(context, "Проверка связи: Ok")
                } catch (e: Exception) {
                    error(context, e.message.orEmpty())
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


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        label = { Text("Сервер") },
                        value = appSettings.backendUrl.orEmpty(),
                        onValueChange = { appSettings.backendUrl = it },
                        enabled = !checkingServer
                    )
                    if (!checkingServer) {
                        IconButton(Icons.Outlined.Check, onClick = ::checkServer)
                    } else {
                        CircularProgressIndicator()
                    }
                }

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