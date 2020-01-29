package com.stefattorusso.simpleconverter


import com.stefattorusso.simpleconverter.base.BaseApplication
import timber.log.Timber

class MyApplication : BaseApplication(){

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
