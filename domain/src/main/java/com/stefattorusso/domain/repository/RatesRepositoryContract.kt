package com.stefattorusso.domain.repository

import com.stefattorusso.domain.RateDomain
import io.reactivex.Flowable

interface RatesRepositoryContract {

    fun retrieveLatest(base: String): Flowable<List<RateDomain>>
}