package com.tavrida.utils

fun Any?.toStringOrEmpty() = this?.toString() ?: ""

fun Any.println() = println(this)