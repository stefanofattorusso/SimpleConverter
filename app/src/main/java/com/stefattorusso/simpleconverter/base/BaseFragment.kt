package com.stefattorusso.simpleconverter.base

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.stefattorusso.simpleconverter.R
import com.stefattorusso.simpleconverter.model.ErrorModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected abstract val viewModelClass: Class<VM>

    protected lateinit var viewModel: VM

    override fun onAttach(context: Context) {
        injectMembers()
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(viewModelClass)
    }

    protected open fun injectMembers() {
        AndroidSupportInjection.inject(this)
    }

    protected fun showError(errorModel: ErrorModel) {
        showError(errorModel.throwable?.localizedMessage ?: getString(R.string.generic_error_title))
    }

    protected fun showError(errorMessage: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.dialog_error_title))
            .setMessage(errorMessage)
            .setPositiveButton(getString(R.string.dialog_error_positive), null)
            .create()
            .show()
    }
}
