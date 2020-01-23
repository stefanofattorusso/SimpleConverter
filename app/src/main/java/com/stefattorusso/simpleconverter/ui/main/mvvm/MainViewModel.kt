package com.stefattorusso.simpleconverter.ui.main.mvvm

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.stefattorusso.simpleconverter.base.BaseViewModel
import com.stefattorusso.simpleconverter.domain.RateDomain
import com.stefattorusso.simpleconverter.domain.usecase.GetLatestUseCaseContract
import com.stefattorusso.simpleconverter.model.ErrorModel
import com.stefattorusso.simpleconverter.model.RateModel
import com.stefattorusso.simpleconverter.utils.MyOpenClass
import com.stefattorusso.simpleconverter.utils.toModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber
import java.math.BigDecimal
import javax.inject.Inject

@MyOpenClass
class MainViewModel @Inject constructor(
    private val getLatestUseCase: GetLatestUseCaseContract
) : BaseViewModel() {

    private var mDisposable: Disposable? = null

    private var mSelectedRate = RateDomain("EUR", BigDecimal.ONE, true)

    private val mSelectedValue = MutableLiveData<BigDecimal>()
    val selectedValue: LiveData<BigDecimal> = mSelectedValue

    private val mRateList = MutableLiveData<List<RateDomain>>()
    private var mUpdatedRateList = MediatorLiveData<List<RateDomain>>().apply {
        value = mutableListOf()

        addSource(mRateList) {
            value = calculateRates(mSelectedValue.value, it)
        }

        addSource(mSelectedValue) {
            mSelectedRate.value = it
            value = calculateRates(it, mRateList.value)
        }
    }
    var rateModelList: LiveData<List<RateModel>> = Transformations.map(mUpdatedRateList) { list ->
        val finalList = list.toMutableList()
        finalList.add(0, mSelectedRate)
        finalList.toList().map { it.toModel() }
    }

    private val mRateSelected = MutableLiveData<Boolean>()
    val rateSelected: LiveData<Boolean> = mRateSelected

    init {
        loadDefaultRates()
    }

    fun loadData(base: String) {
        mDisposable = getLatestUseCase.getLatest(base)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : ResourceSubscriber<List<RateDomain>>() {
                override fun onComplete() {
                    Log.i("TAG", "onComplete")
                }

                override fun onNext(t: List<RateDomain>?) {
                    mRateList.value = t
                }

                override fun onError(e: Throwable) {
                    mThrowable.value = ErrorModel(throwable = e)
                }

            })
            .addTo(mCompositeDisposable)
    }

    fun onItemSelected(position: Int) {
        if (position > 0) {
            mDisposable?.dispose()
            mRateList.value?.get(position - 1)?.let {
                mSelectedRate = it.apply {
                    base = true
                    value = BigDecimal.ONE
                }
                mRateSelected.value = true
            }
            loadData(mSelectedRate.code)
        }
    }

    fun onRateChange(value: String) {
        mSelectedValue.value = value.toBigDecimal()
    }

    @VisibleForTesting
    fun getSelectedRate() = mSelectedRate

    private fun loadDefaultRates() {
        mSelectedValue.value = BigDecimal.ONE
        loadData(mSelectedRate.code)
    }

    private fun calculateRates(base: BigDecimal?, list: List<RateDomain>?): List<RateDomain> {
        val mutableList = mutableListOf<RateDomain>()
        list?.mapTo(mutableList) { current ->
            RateDomain(
                current.code,
                base?.multiply(current.value) ?: current.value,
                false
            )
        }
        return mutableList.toList()
    }
}
