package com.stefattorusso.simpleconverter.di.module

import com.stefattorusso.domain.repository.RatesRepositoryContract
import com.stefattorusso.simpleconverter.data.repository.RatesRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    internal abstract fun ratesRepository(repository: RatesRepository): RatesRepositoryContract
}