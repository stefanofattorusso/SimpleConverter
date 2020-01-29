package com.stefattorusso.data.network

import com.stefattorusso.data.local.room.RateDatabase

interface BaseResponse {
    fun saveResponseToDb(database: RateDatabase)
}