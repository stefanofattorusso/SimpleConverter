package com.stefattorusso.simpleconverter.ui.main

import android.app.Activity

import com.stefattorusso.simpleconverter.base.BaseActivityModule
import com.stefattorusso.simpleconverter.di.scope.ActivityScope
import com.stefattorusso.simpleconverter.di.scope.FragmentScope
import com.stefattorusso.simpleconverter.ui.main.mvvm.MainFragment
import com.stefattorusso.simpleconverter.ui.main.mvvm.MainFragmentModule

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [BaseActivityModule::class])
abstract class MainActivityModule {

    @Binds
    @ActivityScope
    internal abstract fun activity(activity: MainActivity): Activity

    @FragmentScope
    @ContributesAndroidInjector(modules = [MainFragmentModule::class])
    internal abstract fun fragmentInjector(): MainFragment
}
