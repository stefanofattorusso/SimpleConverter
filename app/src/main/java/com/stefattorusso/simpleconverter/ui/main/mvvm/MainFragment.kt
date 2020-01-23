package com.stefattorusso.simpleconverter.ui.main.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stefattorusso.simpleconverter.R
import com.stefattorusso.simpleconverter.base.BaseFragment
import com.stefattorusso.simpleconverter.model.RateModel
import com.stefattorusso.simpleconverter.ui.main.MainAdapter
import com.stefattorusso.simpleconverter.utils.hideSoftKeyboard
import kotlinx.android.synthetic.main.fragment_main.*

open class MainFragment : BaseFragment<MainViewModel>() {

    private lateinit var mAdapter: MainAdapter

    override val viewModelClass: Class<MainViewModel>
        get() = MainViewModel::class.java

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeData()
    }

    private fun setupViews() {
        mAdapter = MainAdapter(viewModel)
        with(recycler_view) {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            itemAnimator = null
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                        activity?.hideSoftKeyboard()
                        this@MainFragment.view?.requestFocus()
                    }
                }
            })
        }
    }

    private fun observeData() {
        viewModel.rateModelList.observe(viewLifecycleOwner, Observer {
            recycler_view.visibility = VISIBLE
            val mutableList = mutableListOf<RateModel>()
            mutableList.addAll(it)
            mAdapter.updateData(mutableList)
        })

        viewModel.rateSelected.observe(viewLifecycleOwner, Observer {
            (recycler_view.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            showError(it)
        })
    }
}
