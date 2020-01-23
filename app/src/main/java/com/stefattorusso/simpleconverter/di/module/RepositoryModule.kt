package com.stefattorusso.simpleconverter.di.module

import com.stefattorusso.data.repository.RatesRepository
import com.stefattorusso.domain.repository.RatesRepositoryContract
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    internal abstract fun ratesRepository(repository: RatesRepository): RatesRepositoryContract
}