package com.tavrida.energysales.ui.components.common

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.util.function.ToDoubleBiFunction

fun String.text() = @Composable { Text(this) }

@Composable
fun TODO_UI(text: String) {
    Text(text = "TODO: $text", color = Color.Red)
}