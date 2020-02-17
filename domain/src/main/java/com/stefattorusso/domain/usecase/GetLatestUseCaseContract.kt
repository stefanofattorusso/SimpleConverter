package com.stefattorusso.domain.usecase

import com.stefattorusso.domain.RateDomain
import io.reactivex.Flowable

interface GetLatestUseCaseContract {

    fun getLatest(base: String): Flowable<List<RateDomain>>
}