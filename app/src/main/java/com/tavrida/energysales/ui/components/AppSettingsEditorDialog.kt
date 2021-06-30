package com.tavrida.energysales.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tavrida.energysales.AppSettings
import com.tavrida.energysales.EditableAppSettings
import com.tavrida.utils.IconButton
import com.tavrida.utils.rememberMutableStateOf

@Composable
fun AppSettingsEditorDialog(appSettings: EditableAppSettings, onDismiss: () -> Unit) {
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
                    onValueChange = { appSettings.backendUrl = it })
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
                onClick = {
                    appSettings.apply()
                    onDismiss()
                }
            )
        }
    )
}