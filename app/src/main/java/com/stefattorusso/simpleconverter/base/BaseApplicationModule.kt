package com.stefattorusso.simpleconverter.base


import android.app.Application
import android.content.Context
import com.stefattorusso.simpleconverter.di.module.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(
    includes = arrayOf(
        ActivityBuilderModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
        DatabaseModule::class
    )
)
abstract class BaseApplicationModule {

    @Binds
    @Singleton
    internal abstract fun application(application: BaseApplication): Application

    @Binds
    @Singleton
    internal abstract fun context(application: Application): Context
}
