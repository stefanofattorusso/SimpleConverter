package com.stefattorusso.simpleconverter.data

import com.stefattorusso.simpleconverter.data.entity.RatesContainerEntity
import com.stefattorusso.simpleconverter.data.repository.RatesRepository
import com.stefattorusso.simpleconverter.data.repository.RatesRepositoryContract
import com.stefattorusso.simpleconverter.domain.RateDomain
import com.stefattorusso.simpleconverter.network.retrofit.AppRetrofitService
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal

@RunWith(MockitoJUnitRunner::class)
class RepositoryTest {

    @Mock
    lateinit var mService: AppRetrofitService

    private lateinit var mRepository: RatesRepositoryContract

    private val mContainerEntity = RatesContainerEntity(
        base = "EUR",
        date = "2018-09-06",
        rates = mapOf(
            "AUD" to 1.6101,
            "BGN" to 1.9482,
            "BRL" to 4.7732
        )
    )

    private val mRatesList = listOf(
        RateDomain(
            code = "AUD",
            value = BigDecimal.valueOf(1.6101),
            base = false
        ),
        RateDomain(
            code = "BGN",
            value = BigDecimal.valueOf(1.9482),
            base = false
        ),
        RateDomain(
            code = "BRL",
            value = BigDecimal.valueOf(4.7732),
            base = false
        )
    )

    @Before
    fun init() {

        Mockito.`when`(mService.retrieveLatest("EUR")).thenReturn(Single.just(mContainerEntity))

        mRepository = RatesRepository(mService)
    }

    @Test
    fun should_should_convert_container_to_list_of_rates(){

        mRepository.retrieveLatest("EUR")
            .test()
            .assertValues(mRatesList)

    }
}