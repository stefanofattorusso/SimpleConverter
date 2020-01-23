package com.stefattorusso.simpleconverter.di.module

import android.content.Context
import com.stefattorusso.data.network.retrofit.AppRetrofitService
import com.stefattorusso.simpleconverter.BuildConfig
import com.stefattorusso.simpleconverter.R
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
object NetworkModule {

    private const val TIMEOUT = 30                          // 30 sec
    private const val CONNECTION_TIMEOUT = 10               // 10 sec

    @JvmStatic
    @Provides
    @Reusable
    internal fun httpLoggingInterceptorLevel(): HttpLoggingInterceptor.Level {
        return if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
    }

    // Interceptors ====================================================================================================

    @JvmStatic
    @Provides
    @Reusable
    internal fun httpLoggingInterceptor(loggingLevel: HttpLoggingInterceptor.Level): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = loggingLevel
        }
    }

    // OKHttpClient ====================================================================================================

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
            .connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .build()
    }

    // Retrofit ========================================================================================================

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofit(context: Context, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.api_domain))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    // RetrofitService =================================================================================================

    @JvmStatic
    @Provides
    @Reusable
    internal fun appRetrofitService(retrofit: Retrofit): AppRetrofitService {
        return retrofit.create(AppRetrofitService::class.java)
    }
}