package com.stefattorusso.simpleconverter.domain

import java.math.BigDecimal

data class RateDomain(
    var code: String,
    var value: BigDecimal,
    var base: Boolean
)