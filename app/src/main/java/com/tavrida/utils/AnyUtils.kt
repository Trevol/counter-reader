package com.tavrida.utils

import java.time.Instant

fun Any?.toStringOrEmpty() = this?.toString().orEmpty()

fun Any.println() = println(this)
fun Any?.printlnStamped() = println("${Instant.now()}: $this")

fun Any.padStartEx(length: Int, padChar: Char) = toString().padStart(length, padChar)