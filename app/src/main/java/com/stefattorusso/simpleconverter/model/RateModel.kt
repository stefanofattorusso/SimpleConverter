package com.stefattorusso.simpleconverter.model

data class RateModel(
    val code: String,
    val name: String,
    val value: String,
    val base: Boolean,
    val flag: Int
)