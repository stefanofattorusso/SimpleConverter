package com.stefattorusso.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stefattorusso.data.local.room.RateDatabase

@Entity(tableName = RateDatabase.DATABASE_NAME)
data class RateEntity(
    @PrimaryKey var code: String,
    var value: String,
    var base: Boolean
)