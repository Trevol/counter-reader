package com.tavrida.utils

fun Any?.toStringOrEmpty() = this?.toString() ?: ""

fun Any.println() = println(this)

fun Any.padStartEx(length: Int, padChar: Char) = toString().padStart(length, padChar)