package com.stefattorusso.simpleconverter.di.module

import com.stefattorusso.simpleconverter.domain.usecase.GetLatestUseCase
import com.stefattorusso.simpleconverter.domain.usecase.GetLatestUseCaseContract
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class UseCaseModule {

    @Binds
    @Singleton
    internal abstract fun getLatestUseCase(repository: GetLatestUseCase): GetLatestUseCaseContract
}