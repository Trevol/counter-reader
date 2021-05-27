package com.tavrida.energysales.data_contract

import kotlinx.serialization.Serializable

@Serializable
data class CounterReadingIdMapping(val id: Int, val serverId: Int)