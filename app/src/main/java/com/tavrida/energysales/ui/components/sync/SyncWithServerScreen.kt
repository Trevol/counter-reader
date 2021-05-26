package com.tavrida.energysales.ui.components.sync

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.tavrida.energysales.ui.components.common.BackButton
import com.tavrida.utils.rememberMutableStateOf
import com.tavrida.utils.suppressedClickable
import java.lang.Exception

@Composable
fun SyncWithServerScreen(
    testMode: Boolean,
    sync: suspend (testMode: Boolean) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var done by rememberMutableStateOf(false)
    LaunchedEffect(key1 = Unit) {
        try {
            sync(testMode)
        } catch (e: Exception) {
            com.tavrida.utils.error(context, e.message.orEmpty())
        }
        done = true
    }
    BackHandler(enabled = false, onBack = onClose)
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .suppressedClickable(),
        topBar = {
            BackButton(onClose)
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            CircularProgressIndicator()
            if (done) {
                Button(onClick = onClose) {
                    Text(text = "Close")
                }
            }
        }
    }

}