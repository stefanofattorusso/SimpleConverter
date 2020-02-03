package com.stefattorusso.data.repository

import com.stefattorusso.data.local.entity.RateEntity
import com.stefattorusso.data.local.room.RateDatabase
import com.stefattorusso.data.network.NetworkBoundResource
import com.stefattorusso.data.network.entity.RatesContainerEntity
import com.stefattorusso.data.network.retrofit.AppRetrofitService
import com.stefattorusso.domain.RateDomain
import com.stefattorusso.domain.repository.RatesRepositoryContract
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import java.math.BigDecimal
import javax.inject.Inject

class RatesRepository @Inject constructor(
    private val service: AppRetrofitService,
    private val database: RateDatabase
) : RatesRepositoryContract {

    override fun retrieveLatest(base: String): Flowable<List<RateDomain>> {
        return object : NetworkBoundResource<RatesContainerEntity, RateEntity>(){

            override fun shouldFetch(): Boolean {
                return true
            }

            override fun createCall(): Observable<RatesContainerEntity> {
                return service.retrieveLatest(base).toObservable()
            }

            override fun loadFromDb(): Flowable<RateEntity> {
                return database.rateDao().load(base)
            }

            override fun saveCallResult(resultType: RatesContainerEntity) {
                val entity = RateEntity(
                    resultType.base ?: "",
                    resultType.date ?: "",
                    resultType.rates ?: emptyMap()
                )
                database.rateDao().save(entity)
            }

        }.asObservable()
            .map { mapData(it.data?.rates ?: emptyMap()) }
            .toFlowable(BackpressureStrategy.LATEST)
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