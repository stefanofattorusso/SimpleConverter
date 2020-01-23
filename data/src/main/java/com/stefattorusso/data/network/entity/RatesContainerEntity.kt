package com.stefattorusso.data.network.entity

data class RatesContainerEntity(
    var base: String? = null,
    var date: String? = null,
    var rates: Map<String, Double>? = null
)