package com.tavrida.energysales.data_contract

import kotlinx.serialization.Serializable

@Serializable
data class CounterReadingSyncRequest(val items: List<CounterReadingSyncItem>)