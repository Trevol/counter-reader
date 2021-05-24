package com.tavrida.energysales.ui.components.common

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.runtime.Composable

@Composable
fun ScanByCameraFloatingButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(Icons.Outlined.PhotoCamera, null)
    }
}