package com.stefattorusso.simpleconverter.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stefattorusso.simpleconverter.di.ViewModelKey
import com.stefattorusso.simpleconverter.di.ViewModelProviderFactory
import com.stefattorusso.simpleconverter.di.scope.ActivityScope
import com.stefattorusso.simpleconverter.ui.main.mvvm.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @Binds
    @ActivityScope
    abstract fun viewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(MainViewModel::class)
    internal abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

}