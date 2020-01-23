package com.stefattorusso.simpleconverter.data.repository

import com.stefattorusso.domain.RateDomain
import com.stefattorusso.domain.repository.RatesRepositoryContract
import com.stefattorusso.simpleconverter.network.retrofit.AppRetrofitService
import io.reactivex.Single
import java.math.BigDecimal
import javax.inject.Inject

class RatesRepository @Inject constructor(
    private val service: AppRetrofitService
) : RatesRepositoryContract {

    override fun retrieveLatest(base: String): Single<List<RateDomain>> {
        return service.retrieveLatest(base)
            .map { mapData(it.rates ?: emptyMap()) }
    }

    private fun mapData(map: Map<String, Double>): List<RateDomain> {
        val list = mutableListOf<RateDomain>()
        for (m: Map.Entry<String, Double> in map) {
            list.add(
                RateDomain(
                    m.key,
                    BigDecimal.valueOf(m.value),
                    false
                )
            )
        }
        return list.sortedWith(compareBy(RateDomain::code))
    }
}