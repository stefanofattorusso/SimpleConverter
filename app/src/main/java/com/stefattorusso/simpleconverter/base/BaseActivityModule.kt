package com.stefattorusso.simpleconverter.base

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.stefattorusso.simpleconverter.di.module.ViewModelModule
import com.stefattorusso.simpleconverter.di.scope.ActivityScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [ViewModelModule::class])
abstract class BaseActivityModule {

    @Binds
    @Named(ACTIVITY_CONTEXT)
    @ActivityScope
    internal abstract fun activityContext(activity: Activity): Context

    @Module
    companion object {

        const val ACTIVITY_CONTEXT = "BaseActivityModule.activityContext"

        @JvmStatic
        @Provides
        @ActivityScope
        internal fun activity(activity: Activity): AppCompatActivity {
            return activity as AppCompatActivity
        }

        @JvmStatic
        @Provides
        @ActivityScope
        internal fun resources(activity: AppCompatActivity): Resources {
            return activity.resources
        }

        @JvmStatic
        @Provides
        @ActivityScope
        internal fun fragmentManager(activity: AppCompatActivity): FragmentManager {
            return activity.supportFragmentManager
        }
    }
}
