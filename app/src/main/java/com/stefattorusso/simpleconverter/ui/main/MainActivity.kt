package com.stefattorusso.simpleconverter.ui.main

import android.os.Bundle
import com.stefattorusso.simpleconverter.R
import com.stefattorusso.simpleconverter.base.BaseActivity
import com.stefattorusso.simpleconverter.ui.main.mvvm.MainFragment
import com.stefattorusso.simpleconverter.utils.getFragment
import com.stefattorusso.simpleconverter.utils.loadFragment
import com.stefattorusso.simpleconverter.utils.newInstance

class MainActivity : BaseActivity() {

    private var mFragment: MainFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            mFragment = MainFragment().newInstance(intent.extras)
            mFragment?.let { loadFragment(R.id.container, it) }
        } else {
            mFragment = getFragment(R.id.container) as? MainFragment
        }
    }
}
