package com.nourelden515.clotheingsuggester.ui.location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.nourelden515.clotheingsuggester.R
import com.nourelden515.clotheingsuggester.base.BaseFragment
import com.nourelden515.clotheingsuggester.data.RepositoryImpl
import com.nourelden515.clotheingsuggester.data.source.local.LocalDataSourceImpl
import com.nourelden515.clotheingsuggester.data.source.remote.RemoteDataSourceImpl
import com.nourelden515.clotheingsuggester.databinding.FragmentLocationBinding
import com.nourelden515.clotheingsuggester.utils.onClickBackFromNavigation
import com.nourelden515.clotheingsuggester.utils.shared.SharedPreferencesUtils

class LocationFragment :
    BaseFragment<LocationViewModel, FragmentLocationBinding, RepositoryImpl>() {

    override fun getViewModel() = LocationViewModel::class.java

    override fun getFragmentRepository() = RepositoryImpl(
        RemoteDataSourceImpl(),
        LocalDataSourceImpl(requireContext()),
        SharedPreferencesUtils(requireContext())
    )

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var requestPermissionLauncher = registerCallBack()

    override val TAG = this::class.simpleName.toString()

    override fun getViewBinding() = FragmentLocationBinding.inflate(layoutInflater)

    override fun setUp() {

        viewModel.getLatLon()
        viewModel.locationValid.observe(viewLifecycleOwner) {
            if (it == true) {
                viewModel.location.value?.let { it1 -> navigateToHome(it1) }
                viewModel.onCompleteNavigation()
            } else {
                log("NOT Valid")
            }
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            requireContext()
        )

        binding.buttonContinue.setOnClickListener {
            getLatestLocation()
        }
        onClickBackFromNavigation()
    }

    private fun navigateToHome(location: Pair<Float?, Float?>) {
        if (location.first != 0F && location.second != 0F) {
            val action = LocationFragmentDirections
                .actionLocationFragmentToHomeFragment(location.first!!, location.second!!)
            findNavController(requireView()).navigate(action)
        }
    }

    /**
     * Get User's last location if exist and if not -> request current location.
     */
    @Suppress("MissingPermission")
    private fun getLatestLocation() {
        if (isUserLocationEnabled()) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { resultLocation ->
                if (resultLocation.isSuccessful) {
                    resultLocation.run {
                        if (this.result != null) {
                            showSaveLocationRationaleDialog(
                                this.result
                            )
                        } else {
                            showRationaleDialog(
                                getString(R.string.location_is_disabled),
                                getString(R.string.please_enable_your_location_settings)
                            )
                        }
                    }
                } else {
                    getCurrentLocation()
                }
            }
        }
    }

    /**
     * Get current location.
     */
    @Suppress("MissingPermission")
    private fun getCurrentLocation() {
        val priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val cancellationTokenSource = CancellationTokenSource()

        fusedLocationProviderClient.getCurrentLocation(priority, cancellationTokenSource.token)
            .addOnSuccessListener { location ->
                if (location != null) {
                    showSaveLocationRationaleDialog(
                        location
                    )
                } else {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.please_enable_your_location_settings),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
    }

    /**
     * Register callback to request location permissions
     */
    private fun registerCallBack(): ActivityResultLauncher<Array<String>> {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    false
                ) -> {
                    //Enable MyLocationButton.
                    isUserLocationEnabled()
                }

                permissions.getOrDefault(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    false
                ) -> {
                    log(getString(R.string.only_approximate_location_access_granted))
                    getLatestLocation()
                }

                else -> {
                    log(getString(R.string.location_permission_was_not_granted))
                }
            }
        }
        return requestPermissionLauncher
    }

    /**
     * Check if the location permissions is granted and request it if not.
     */
    private fun isUserLocationEnabled(): Boolean {
        var result = false
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // This condition only becomes true if the user has denied the permission previously
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                && shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                showRationaleDialog(
                    getString(R.string.rationale_title), getString(R.string.rationale_desc)
                )
            } else {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        } else {
            log(getString(R.string.location_permissions_is_granted))
            result = true
        }
        return result
    }

    /**
     * Shows dialog to inform the user why we need those permissions.
     */
    private fun showRationaleDialog(title: String, message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                getString(R.string.ok)
            ) { _, _ ->
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
            .show()
    }

    private fun showSaveLocationRationaleDialog(
        location: Location
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.make_this_your_default_location))
            .setMessage(getString(R.string.we_can_save_your_location_for_you))
            .setPositiveButton(
                getString(R.string.yes)
            ) { _, _ ->
                viewModel.saveLatLon(
                    location.latitude.toFloat(),
                    location.longitude.toFloat()
                )
                navigateToHome(Pair(location.latitude.toFloat(), location.longitude.toFloat()))
            }
            .setNegativeButton(
                getString(R.string.no)
            ) { _, _ ->
                navigateToHome(Pair(location.latitude.toFloat(), location.longitude.toFloat()))
            }.show()
    }
}