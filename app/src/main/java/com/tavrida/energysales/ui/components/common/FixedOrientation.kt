package com.tavrida.energysales.ui.components.common

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.tavrida.utils.Orientation
import com.tavrida.utils.fixCurrentOrientation

@Composable
fun FixedOrientation(content: @Composable (Orientation) -> Unit) {
    val activity = LocalContext.current as Activity
    val original = remember {
        activity.requestedOrientation
    }

    val orientation = activity.fixCurrentOrientation()
    content(orientation)

    DisposableEffect(key1 = Unit) {
        onDispose {
            activity.requestedOrientation = original
        }
    }
}