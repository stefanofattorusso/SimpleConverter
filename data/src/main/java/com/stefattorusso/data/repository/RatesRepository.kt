package com.stefattorusso.data.repository

import android.content.Context
import com.stefattorusso.data.isOnLine
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
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RatesRepository @Inject constructor(
    private val context: Context,
    private val service: AppRetrofitService,
    private val database: RateDatabase
) : RatesRepositoryContract {

    override fun startRetrievingRates(base: String): Flowable<List<RateDomain>> {
        return object : NetworkBoundResource<RatesContainerEntity, RateEntity>() {

            override fun shouldFetch(): Boolean = context.isOnLine()

            override fun createCall(): Observable<RatesContainerEntity> {
                return service.retrieveLatest(base)
                    .repeatWhen {
                        Observable.interval(1000, TimeUnit.MILLISECONDS)
                            .toFlowable(BackpressureStrategy.LATEST)
                    }.toObservable()
            }

            override fun loadFromDb(): Flowable<RateEntity> = database.rateDao().load(base)

            override fun saveCallResult(resultType: RatesContainerEntity) {
                val entity = RateEntity(
                    resultType.base ?: "",
                    resultType.date ?: "",
                    resultType.rates ?: emptyMap()
                )
                database.rateDao().save(entity)
            }

        }.asFlowable().map {
            mapData(it.data?.rates ?: emptyMap())
        }
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