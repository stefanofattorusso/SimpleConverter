package com.stefattorusso.domain.repository

import io.reactivex.Single

interface RatesRepositoryContract {

    fun retrieveLatest(base: String): Single<List<com.stefattorusso.domain.RateDomain>>
}