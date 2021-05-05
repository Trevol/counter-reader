package com.tavrida.utils

fun Double?.noTrailingZero() = this?.noTrailingZero().orEmpty()

fun Double.noTrailingZero(): String {
    val str = toStringNoExponent()
    return if (str.at(-1) == '0' && str.at(-2) == '.') {
        str.substring(0, str.length - 2)
    } else
        str
}

fun Double.toStringNoExponent(): String = toBigDecimal().toPlainString()

private fun String.at(index: Int): Char {
    if (index < 0)
        return this[this.length + index]
    return this[index]
}