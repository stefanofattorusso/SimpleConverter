package com.stefattorusso.simpleconverter.base

import com.stefattorusso.simpleconverter.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

abstract class BaseApplication : DaggerApplication(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return AppInjector.init(this)
    }

    override fun androidInjector(): AndroidInjector<Any>? {
        return androidInjector
    }
}
