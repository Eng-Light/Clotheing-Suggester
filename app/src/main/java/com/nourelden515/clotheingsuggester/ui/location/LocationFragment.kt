package com.nourelden515.clotheingsuggester.ui.location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.nourelden515.clotheingsuggester.R
import com.nourelden515.clotheingsuggester.base.BaseFragment
import com.nourelden515.clotheingsuggester.data.Repository
import com.nourelden515.clotheingsuggester.data.RepositoryImpl
import com.nourelden515.clotheingsuggester.data.source.RemoteDataSourceImpl
import com.nourelden515.clotheingsuggester.databinding.FragmentLocationBinding
import com.nourelden515.clotheingsuggester.ui.home.HomeFragment
import com.nourelden515.clotheingsuggester.utils.onClickBackFromNavigation
import com.nourelden515.clotheingsuggester.utils.replaceFragment
import com.nourelden515.clotheingsuggester.utils.shared.SharedPreferencesInterface
import com.nourelden515.clotheingsuggester.utils.shared.SharedPreferencesUtils

class LocationFragment : BaseFragment<FragmentLocationBinding>(), LocationView {

    private val sharedPreferencesUtils: SharedPreferencesInterface by lazy {
        SharedPreferencesUtils(requireContext())
    }
    private val repository: Repository by lazy {
        RepositoryImpl(
            RemoteDataSourceImpl(),
            sharedPreferencesUtils
        )
    }
    private val presenter by lazy {
        LocationPresenter(
            repository, this
        )
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var requestPermissionLauncher = registerCallBack()

    override val TAG = this::class.simpleName.toString()

    override fun getViewBinding() = FragmentLocationBinding.inflate(layoutInflater)

    override fun setUp() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            requireContext()
        )

        binding.buttonContinue.setOnClickListener {
            getLatestLocation()
        }
        onClickBackFromNavigation()
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

    private fun navigateToHomeFragment(location: Location) {
        val homeFragment = HomeFragment.newInstance(location.latitude, location.longitude)
        replaceFragment(homeFragment)
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
                presenter.saveLatLon(
                    location.latitude.toFloat(),
                    location.longitude.toFloat()
                )
                navigateToHomeFragment(location)
            }
            .setNegativeButton(
                getString(R.string.no)
            ) { _, _ ->
                navigateToHomeFragment(location)
            }.show()
    }
}