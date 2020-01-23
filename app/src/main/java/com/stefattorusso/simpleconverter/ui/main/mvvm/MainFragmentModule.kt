package com.stefattorusso.simpleconverter.ui.main.mvvm

import androidx.fragment.app.Fragment


import com.stefattorusso.simpleconverter.di.scope.FragmentScope

import dagger.Binds
import dagger.Module

@Module
abstract class MainFragmentModule {

    @Binds
    @FragmentScope
    abstract fun fragment(fragment: MainFragment): Fragment
}
