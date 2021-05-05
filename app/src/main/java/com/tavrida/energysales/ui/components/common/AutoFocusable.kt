package com.tavrida.energysales.ui.components.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import kotlinx.coroutines.launch

@Composable
fun AutoFocusable(content: @Composable AutoFocusScope.() -> Unit) {
    val scope = remember { AutoFocusScope() }
    LaunchedEffect(key1 = Unit) {
        launch { scope.requestFocus() }
    }
    scope.content()
}

class AutoFocusScope(private val focusRequester: FocusRequester = FocusRequester()) {
    fun requestFocus() = focusRequester.requestFocus()
    fun Modifier.focusRequester() = focusRequester(focusRequester)
}