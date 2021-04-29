package com.tavrida.utils

fun String.filterToInt(): Int? {
    return filter { it.isDigit() }.toIntOrNull()
}

fun String.filterToFloat(): Float? {
    return removeNonFloatChars().toFloatOrNull()
}

fun String.filterToDouble(): Double? {
    return removeNonFloatChars().toDoubleOrNull()
}

private fun String.removeNonFloatChars() =
    filter { it.isDigit() || it == DECIMAL_POINT }
        .removeExtraDecimalPoints()

private fun String.removeExtraDecimalPoints(): String {
    val indexes = withIndex()
        .filter { (_, c) -> c == DECIMAL_POINT }
        .map { (i, _) -> i }
    if (indexes.size <= 1)
        return this
    return indexes.subList(1, indexes.lastIndex + 1)
        .reversed()
        .let { extraIndexes ->
            removeIndexes(extraIndexes)
        }
}

private fun String.removeIndexes(indexes: List<Int>, indexesInDescOrder: Boolean = true): String {
    val indexes = if (indexesInDescOrder) indexes else indexes.sortedByDescending { it }
    var result = this
    for (i in indexes) {
        result = result.removeRange(i, i + 1)
    }
    return result
}

private const val DECIMAL_POINT = '.'

