package com.stefattorusso.simpleconverter.di.module

import com.stefattorusso.simpleconverter.di.scope.ActivityScope
import com.stefattorusso.simpleconverter.ui.main.MainActivity
import com.stefattorusso.simpleconverter.ui.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilderModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(MainActivityModule::class))
    abstract fun mainActivity(): MainActivity

}