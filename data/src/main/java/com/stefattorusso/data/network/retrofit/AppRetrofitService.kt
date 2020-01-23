package com.stefattorusso.data.network.retrofit

import com.stefattorusso.data.network.entity.RatesContainerEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface AppRetrofitService {

    @GET("latest")
    fun retrieveLatest(
        @Query("base") base: String
    ): Single<RatesContainerEntity>
}