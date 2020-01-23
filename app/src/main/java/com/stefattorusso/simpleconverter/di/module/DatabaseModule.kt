package com.stefattorusso.simpleconverter.di.module

import android.app.Application
import androidx.room.Room
import com.stefattorusso.data.local.dao.RateDao
import com.stefattorusso.data.local.room.RateDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideRateDatabase(application: Application): RateDatabase = Room.databaseBuilder(
        application,
        RateDatabase::class.java,
        RateDatabase.DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideRateDao(database: RateDatabase): RateDao = database.rateDao()
}