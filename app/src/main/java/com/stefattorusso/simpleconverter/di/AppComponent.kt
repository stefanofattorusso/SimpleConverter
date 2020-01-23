package com.stefattorusso.simpleconverter.di

import com.stefattorusso.simpleconverter.base.BaseApplication
import com.stefattorusso.simpleconverter.base.BaseApplicationModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        AndroidSupportInjectionModule::class,
        BaseApplicationModule::class
    )
)
interface AppComponent : AndroidInjector<BaseApplication> {

    @Component.Factory
    abstract class Builder : AndroidInjector.Factory<BaseApplication>

    override fun inject(instance: BaseApplication)
}