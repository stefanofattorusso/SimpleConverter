package com.stefattorusso.simpleconverter.di.module

import com.stefattorusso.simpleconverter.data.repository.RatesRepository
import com.stefattorusso.simpleconverter.data.repository.RatesRepositoryContract
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    internal abstract fun ratesRepository(repository: RatesRepository): RatesRepositoryContract
}