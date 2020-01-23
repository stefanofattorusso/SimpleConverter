package com.stefattorusso.simpleconverter.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.stefattorusso.data.entity.RatesContainerEntity
import com.stefattorusso.domain.usecase.GetLatestUseCaseContract
import com.stefattorusso.simpleconverter.RxSchedulerRule
import com.stefattorusso.simpleconverter.model.ErrorModel
import com.stefattorusso.simpleconverter.model.RateModel
import com.stefattorusso.simpleconverter.ui.main.mvvm.MainViewModel
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @Mock
    lateinit var mGetRateListUseCase: GetLatestUseCaseContract
    @Mock
    lateinit var ratesDataObserver: Observer<List<RateModel>>
    @Mock
    lateinit var selectedValueObserver: Observer<BigDecimal>
    @Mock
    lateinit var throwableObserver: Observer<ErrorModel>
    @Captor
    lateinit var itemsCaptor: ArgumentCaptor<List<RateModel>>

    private lateinit var mViewModel: MainViewModel

    private var mSelectedBase = EUR

    private val rateList = listOf(
        com.stefattorusso.domain.RateDomain(
            code = "AUD",
            value = BigDecimal.valueOf(1.6101),
            base = false
        ),
        com.stefattorusso.domain.RateDomain(
            code = "BGN",
            value = BigDecimal.valueOf(1.9482),
            base = false
        ),
        com.stefattorusso.domain.RateDomain(
            code = "BRL",
            value = BigDecimal.valueOf(4.7732),
            base = false
        )
    )

    private val ratesContainer = RatesContainerEntity(
        base = EUR,
        date = "10-11-2019",
        rates = mapOf(
            "AUD" to 1.6101,
            "BGN" to 1.9482,
            "BRL" to 4.7732
        )
    )

    @Before
    fun init() {

        Mockito.`when`(mGetRateListUseCase.getLatest(mSelectedBase)).thenReturn(Flowable.just(rateList))

        mViewModel = MainViewModel(mGetRateListUseCase)

        mViewModel.error.observeForever(throwableObserver)
    }

    @Test
    fun `should check latest rates correctly`() {
        mViewModel.rateModelList.observeForever(ratesDataObserver)

        mViewModel.loadData(mSelectedBase)

        verify(ratesDataObserver, Mockito.atLeastOnce()).onChanged(itemsCaptor.capture())

        assert(!itemsCaptor.value.isNullOrEmpty())

        verify(throwableObserver, Mockito.never()).onChanged(mViewModel.error.value)
    }

    @Test
    fun `should check latest rates content correctly`() {
        mViewModel.rateModelList.observeForever(ratesDataObserver)

        mViewModel.loadData(mSelectedBase)

        verify(ratesDataObserver, Mockito.atLeastOnce()).onChanged(itemsCaptor.capture())

        rateList.forEachIndexed { index, item ->
            assert(itemsCaptor.value[index + 1].code == item.code)
        }

        verify(throwableObserver, Mockito.never()).onChanged(mViewModel.error.value)
    }

    @Test
    fun `should check value inserted correctly`(){
        mViewModel.selectedValue.observeForever(selectedValueObserver)

        mViewModel.onRateChange("10")

        assert(mViewModel.selectedValue.value == BigDecimal("10"))
    }

    @Test
    fun `should check selected rate become base rate`(){
        mViewModel.rateModelList.observeForever(ratesDataObserver)

        mViewModel.loadData(mSelectedBase)

        verify(ratesDataObserver, Mockito.atLeastOnce()).onChanged(itemsCaptor.capture())

        val position = 2

        Mockito.`when`(mGetRateListUseCase.getLatest(rateList[position - 1].code)).thenReturn(Flowable.just(rateList))

        mViewModel.onItemSelected(position)

        val rateModel = itemsCaptor.value?.get(position)

        assert(mViewModel.getSelectedRate().code == rateModel?.code)

        assert(mViewModel.getSelectedRate().base)
    }

    companion object {
        private const val EUR = "EUR"
    }
}