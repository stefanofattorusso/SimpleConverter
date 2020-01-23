package com.stefattorusso.simpleconverter.data.entity

data class RatesContainerEntity(
    var base: String? = null,
    var date: String? = null,
    var rates: Map<String, Double>? = null
)