package com.stefattorusso.data.repository

import com.stefattorusso.data.local.entity.RateEntity
import com.stefattorusso.data.local.room.RateDatabase
import com.stefattorusso.data.network.ApiResponse
import com.stefattorusso.data.network.ApiSuccessResponse
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
        return object : NetworkBoundResource<ApiResponse<RatesContainerEntity>, RateEntity>(){

            override fun shouldFetch(): Boolean {
                return true
            }

            override fun createCall(): Observable<ApiResponse<RatesContainerEntity>> {
                return service.retrieveLatest(base).toObservable()
            }

            override fun loadFromDb(): Flowable<RateEntity> {
                return database.rateDao().load(base)
            }

            override fun saveCallResult(resultType: ApiResponse<RatesContainerEntity>) {
                val result = (resultType as ApiSuccessResponse)
//                database.rateDao().save(result.body)
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