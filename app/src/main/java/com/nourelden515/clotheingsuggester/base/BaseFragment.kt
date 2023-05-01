package com.nourelden515.clotheingsuggester.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.nourelden515.clotheingsuggester.data.Repository
import com.nourelden515.clotheingsuggester.utils.shared.SharedPreferencesInterface
import com.nourelden515.clotheingsuggester.utils.shared.SharedPreferencesUtils

abstract class BaseFragment<VM : ViewModel, VB : ViewBinding, R : Repository> : Fragment() {
    abstract val TAG: String
    private var _binding: VB? = null
    open lateinit var viewModel: VM
    protected val binding get() = _binding!!

    abstract fun getViewBinding(): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding()
        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this, factory)[getViewModel()]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    abstract fun setUp()

    abstract fun getViewModel(): Class<VM>

    abstract fun getFragmentRepository(): R

    protected fun log(value: Any) {
        Log.e(TAG, value.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}