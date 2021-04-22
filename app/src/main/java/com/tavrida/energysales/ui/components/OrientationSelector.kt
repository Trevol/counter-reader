package com.tavrida.energysales.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun OrientationSelector(content: @Composable () -> Unit) {
    OrientationSelector(portrait = content, landscape = content)
}

@Composable
fun OrientationSelector(
    portrait: @Composable () -> Unit,
    landscape: @Composable () -> Unit,
    unexpected: @Composable () -> Unit = {
        Text("Unexpected orientation")
    }
) {
    when {
        OrientationHelper.portrait() -> {
            portrait()
        }
        OrientationHelper.landscape() -> {
            landscape()
        }
        else -> {
            unexpected()
        }
    }
}