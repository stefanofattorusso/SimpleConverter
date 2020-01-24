package com.stefattorusso.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stefattorusso.data.local.RateConverter
import com.stefattorusso.data.local.dao.RateDao
import com.stefattorusso.data.local.entity.RateEntity

@Database(entities = [RateEntity::class], version = 1)
@TypeConverters(RateConverter::class)
abstract class RateDatabase : RoomDatabase(){

    abstract fun rateDao(): RateDao

    companion object {
        const val DATABASE_NAME = "rates"
    }
}