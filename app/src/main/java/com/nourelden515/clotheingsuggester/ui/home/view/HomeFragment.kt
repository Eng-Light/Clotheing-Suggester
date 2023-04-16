package com.nourelden515.clotheingsuggester.ui.home.view

import com.nourelden515.clotheingsuggester.base.BaseFragment
import com.nourelden515.clotheingsuggester.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val TAG = this::class.java.simpleName.toString()
    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun setUp() {

    }

}