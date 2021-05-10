package com.tavrida.energysales.ui.components.common

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.tavrida.energysales.ui.theme.CounterReaderTheme
import com.tavrida.energysales.ui.view_models.CounterReadingViewModel

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

@Composable
private fun OrientationSelectorExample() {
    CounterReaderTheme {
        OrientationSelector(
            portrait = {
                Text(text = "Portrait!!!")
            },
            landscape = {
                Text(text = "Landscape!!!")
            }
        )
    }
}