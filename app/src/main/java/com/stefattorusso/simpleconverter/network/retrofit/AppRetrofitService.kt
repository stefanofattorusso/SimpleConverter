package com.stefattorusso.simpleconverter.network.retrofit

import com.stefattorusso.simpleconverter.data.entity.RatesContainerEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface AppRetrofitService {

    @GET("latest")
    fun retrieveLatest(
        @Query("base") base: String
    ): Single<RatesContainerEntity>
}