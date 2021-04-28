package com.tavrida.energysales.ui.components.common

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

fun String.text() = @Composable { Text(this) }