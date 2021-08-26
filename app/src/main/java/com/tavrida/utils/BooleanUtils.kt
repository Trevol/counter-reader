package com.tavrida.utils

fun Boolean?.orFalse() = this ?: false

inline fun <T> iif(condition: Boolean, trueValue: T, falseValue: T): T =
    if (condition) trueValue else falseValue

@JvmName("iifExtension")
inline fun <T> Boolean.iif(trueValue: T, falseValue: T): T = iif(this, trueValue, falseValue)
