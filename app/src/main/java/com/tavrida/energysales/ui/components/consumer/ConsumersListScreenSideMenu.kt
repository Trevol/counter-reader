package com.tavrida.energysales.ui.components.consumer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tavrida.utils.ClickHandler

@Composable
fun ConsumersListScreenSideMenu(
    onDownloadFromServer: ClickHandler,
    onUploadResultsToServer: ClickHandler,
    onProgressRequest: ClickHandler,
    onSettings: ClickHandler
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
    ) {
        MenuItem(Icons.Outlined.ProductionQuantityLimits, text = "Сделано/Всего", onProgressRequest)
        MenuItem(Icons.Outlined.Download, "Загрузить с сервера", onDownloadFromServer)
        MenuItem(Icons.Outlined.Upload, "Сохранить на сервер", onUploadResultsToServer)
        MenuItem(Icons.Outlined.Settings, "Настройки", onSettings)
    }
}

@Composable
private fun MenuItem(imageVector: ImageVector, text: String, onClick: ClickHandler) {
    MenuItem(onClick = onClick) {
        Icon(imageVector = imageVector, contentDescription = null)
        Text(text = text)
    }
}

@Composable
private fun MenuItem(onClick: ClickHandler, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}
