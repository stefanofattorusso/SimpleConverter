package com.stefattorusso.domain.repository

import com.stefattorusso.domain.RateDomain
import io.reactivex.Flowable

interface RatesRepositoryContract {

    fun startRetrievingRates(base: String): Flowable<List<RateDomain>>
}