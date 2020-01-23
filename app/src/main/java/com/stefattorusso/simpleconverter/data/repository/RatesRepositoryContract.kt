package com.stefattorusso.simpleconverter.data.repository

import com.stefattorusso.simpleconverter.domain.RateDomain
import io.reactivex.Single

interface RatesRepositoryContract {

    fun retrieveLatest(base: String): Single<List<RateDomain>>
}