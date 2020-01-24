package com.stefattorusso.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stefattorusso.data.local.room.RateDatabase

@Entity(tableName = RateDatabase.DATABASE_NAME)
data class RateEntity(
    @PrimaryKey var base: String,
    var date: String,
    var rates: HashMap<String, Double>
)