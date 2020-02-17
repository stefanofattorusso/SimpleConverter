package com.stefattorusso.data.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


abstract class NetworkBoundResource<RawResult, ResultType> {

    private var result: Flowable<Resource<ResultType>>

    init {
        result = if (this.shouldFetch()) {
            this.createCall()
                .doOnNext {
                    saveCallResult(it)
                }
                .flatMap {
                    loadFromDb()
                        .toObservable()
                        .map { Resource.success(it) }
                }
                .doOnError {
                    onFetchFailed(it)
                }
                .onErrorResumeNext { t: Throwable ->
                    loadFromDb()
                        .toObservable()
                        .map { Resource.error(t.localizedMessage, it) }
                }
                .toFlowable(BackpressureStrategy.LATEST)

        } else {
            this.loadFromDb()
                .subscribeOn(Schedulers.io())
                .map { Resource.success(it) }
        }
    }

    fun asObservable(): Observable<Resource<ResultType>> {
        return result.toObservable()
    }

    fun asFlowable(): Flowable<Resource<ResultType>>{
        return result
    }

    protected fun onFetchFailed(t: Throwable) {
        Timber.e(t)
    }

    @WorkerThread
    protected abstract fun saveCallResult(resultType: RawResult)

    @MainThread
    protected abstract fun shouldFetch(): Boolean

    @MainThread
    protected abstract fun loadFromDb(): Flowable<ResultType>

    @MainThread
    protected abstract fun createCall(): Observable<RawResult>

    @MainThread
    protected fun initLoadDb(): Flowable<ResultType> = loadFromDb()

}
