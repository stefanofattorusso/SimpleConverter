package com.stefattorusso.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stefattorusso.data.local.dao.RateDao
import com.stefattorusso.data.local.entity.RateEntity

@Database(entities = [RateEntity::class], version = 1)
abstract class RateDatabase : RoomDatabase(){

    abstract fun rateDao(): RateDao

    companion object {
        const val DATABASE_NAME = "rates"
    }
}