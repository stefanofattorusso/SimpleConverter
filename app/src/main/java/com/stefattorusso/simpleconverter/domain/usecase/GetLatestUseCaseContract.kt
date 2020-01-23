package com.stefattorusso.simpleconverter.domain.usecase

import com.stefattorusso.simpleconverter.domain.RateDomain
import io.reactivex.Flowable

interface GetLatestUseCaseContract {

    fun getLatest(base: String): Flowable<List<RateDomain>>
}