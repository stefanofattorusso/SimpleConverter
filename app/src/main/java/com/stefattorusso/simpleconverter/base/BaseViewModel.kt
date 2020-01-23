package com.stefattorusso.simpleconverter.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stefattorusso.simpleconverter.model.ErrorModel
import com.stefattorusso.simpleconverter.utils.MyOpenClass
import io.reactivex.disposables.CompositeDisposable

@MyOpenClass
abstract class BaseViewModel : ViewModel() {

    protected var mCompositeDisposable = CompositeDisposable()

    protected var mThrowable = MutableLiveData<ErrorModel>()
    val error: LiveData<ErrorModel> = mThrowable

    override fun onCleared() {
        mCompositeDisposable.clear()
        super.onCleared()
    }
}
