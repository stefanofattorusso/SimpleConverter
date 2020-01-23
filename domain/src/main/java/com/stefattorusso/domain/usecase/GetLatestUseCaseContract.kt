package com.stefattorusso.domain.usecase

import io.reactivex.Flowable

interface GetLatestUseCaseContract {

    fun getLatest(base: String): Flowable<List<com.stefattorusso.domain.RateDomain>>
}