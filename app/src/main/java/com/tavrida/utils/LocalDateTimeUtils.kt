package com.tavrida.utils

import java.time.*

fun LocalDateTime.toInstant(offset: ZoneOffset? = ZoneOffset.UTC): Instant = toInstant(offset)
fun LocalDateTime.toEpochMilli(offset: ZoneOffset? = ZoneOffset.UTC) =
    toInstant(offset).toEpochMilli()