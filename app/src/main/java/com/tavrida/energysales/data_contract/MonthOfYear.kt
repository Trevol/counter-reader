package com.tavrida.energysales.data_contract

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Serializable
data class MonthOfYear(val month: Int, val year: Int) : Comparable<MonthOfYear> {
    override operator fun compareTo(other: MonthOfYear): Int {
        val yearComparison = year.compareTo(other.year)
        return if (yearComparison != 0) yearComparison else month.compareTo(other.month)
    }
}

fun MonthOfYear.prevMonth() = firstDate().minusMonths(1).toMonthOfYear()

fun MonthOfYear.firstDate(): LocalDate = LocalDate.of(year, month, 1)
fun MonthOfYear.lastDate(): LocalDate = firstDate().with(TemporalAdjusters.lastDayOfMonth())

fun LocalDate.toMonthOfYear() = MonthOfYear(monthValue, year)