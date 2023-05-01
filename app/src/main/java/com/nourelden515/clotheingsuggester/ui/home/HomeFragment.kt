package com.nourelden515.clotheingsuggester.ui.home

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.nourelden515.clotheingsuggester.R
import com.nourelden515.clotheingsuggester.base.BaseFragment
import com.nourelden515.clotheingsuggester.data.RepositoryImpl
import com.nourelden515.clotheingsuggester.data.models.ApiState
import com.nourelden515.clotheingsuggester.data.source.local.LocalDataSourceImpl
import com.nourelden515.clotheingsuggester.data.source.remote.RemoteDataSourceImpl
import com.nourelden515.clotheingsuggester.databinding.FragmentHomeBinding
import com.nourelden515.clotheingsuggester.utils.onClickBackFromNavigation
import com.nourelden515.clotheingsuggester.utils.shared.SharedPreferencesUtils

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding, RepositoryImpl>() {

    override fun getViewModel() = HomeViewModel::class.java

    override fun getFragmentRepository() = RepositoryImpl(
        RemoteDataSourceImpl(),
        LocalDataSourceImpl(requireContext()),
        SharedPreferencesUtils(requireContext())
    )

    private lateinit var location: Pair<Double, Double>

    private val args: HomeFragmentArgs by navArgs()

    override val TAG = this::class.java.simpleName.toString()
    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun setUp() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        getLocation()
        refreshApp()
        checkLocationAndGetAllData()
        addCallbacks()
        onClickBackFromNavigation()
        addObservers()
    }

    private fun getLocation() {
        location = Pair(args.lat.toDouble(), args.lon.toDouble())
    }

    private fun addObservers() {
        viewModel.weatherResponse.observe(viewLifecycleOwner) {
            if (it is ApiState.Error) {
                showError()
            }
        }
    }

    private fun addCallbacks() {
        binding.buttonResetLocation.setOnClickListener {
            showResetLocationDialog()
        }
    }

    private fun refreshApp() {
        binding.swipeToRefresh.setOnRefreshListener {
            checkLocationAndGetAllData()
            binding.swipeToRefresh.isRefreshing = false
        }
    }

    private fun showError() {
        showSnackBar(getString(R.string.connection_error)) {
            checkLocationAndGetAllData()
        }
    }

    private fun checkLocationAndGetAllData() {
        if (::location.isInitialized) {
            viewModel.getImageIndex()
            viewModel.getImageLastViewedDay()
            viewModel.getWeatherData(location.first, location.second)
        }
    }

    private fun showSnackBar(message: String, action: (() -> Unit)? = null) {
        val snackBar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
        action?.let {
            snackBar.setAction(getString(R.string.retry)) {
                it()
            }
        }
        snackBar.show()
    }

    private fun showResetLocationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_title))
            .setMessage(getString(R.string.accept))
            .setPositiveButton(
                getString(R.string.yes)
            ) { _, _ ->
                viewModel.resetLocation()
                navigateToLocationFragment()
            }
            .setNegativeButton(
                getString(R.string.no)
            ) { _, _ ->
                Snackbar.make(requireView(), getString(R.string.canceled), Snackbar.LENGTH_SHORT)
                    .show()
            }.show()

    }

    private fun navigateToLocationFragment() {
        findNavController().navigate(R.id.action_homeFragment_to_locationFragment)
    }
}