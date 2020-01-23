package com.stefattorusso.simpleconverter.model

data class ErrorModel(
    val throwable: Throwable? = null,
    val code: Int? = 0,
    val resource: Any? = null
)