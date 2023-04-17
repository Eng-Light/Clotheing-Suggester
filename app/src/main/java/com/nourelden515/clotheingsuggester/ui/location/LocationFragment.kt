package com.nourelden515.clotheingsuggester.ui.location

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.nourelden515.clotheingsuggester.R
import com.nourelden515.clotheingsuggester.base.BaseFragment
import com.nourelden515.clotheingsuggester.data.RepositoryImpl
import com.nourelden515.clotheingsuggester.data.source.RemoteDataSourceImpl
import com.nourelden515.clotheingsuggester.databinding.FragmentLocationBinding
import com.nourelden515.clotheingsuggester.ui.home.HomeFragment
import com.nourelden515.clotheingsuggester.utils.SharedPreferencesUtils
import com.nourelden515.clotheingsuggester.utils.replaceFragment

class LocationFragment : BaseFragment<FragmentLocationBinding>(), LocationView {

    private val presenter by lazy {
        LocationPresenter(
            RepositoryImpl(
                RemoteDataSourceImpl(),
                SharedPreferencesUtils(requireContext())
            ), this
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
                                "Location is Disabled",
                                "Please Enable Your Location Settings"
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
                    Toast.makeText(
                        requireContext(),
                        "Please Enable your Location Settings",
                        Toast.LENGTH_LONG
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
                    Toast.makeText(
                        requireContext(),
                        "Only approximate location access granted.",
                        Toast.LENGTH_LONG
                    ).show()
                    getLatestLocation()
                }

                else -> {
                    Toast.makeText(
                        requireContext(),
                        "Location permission was not granted.",
                        Toast.LENGTH_LONG
                    ).show()
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
            Toast.makeText(
                requireContext(), "Location permissions is granted.", Toast.LENGTH_LONG
            ).show()
            result = true
        }
        return result
    }

    /**
     * Shows dialog to inform the user why we need those permissions.
     */
    private fun showRationaleDialog(title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title).setMessage(message).setPositiveButton("Ok") { _, _ ->
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        builder.create().show()
    }

    private fun showSaveLocationRationaleDialog(
        location: Location
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder
            .setTitle("Make This Your Default Location?")
            .setMessage("We can save your location for you :)")
            .setPositiveButton("Ok") { _, _ ->
                presenter.saveLatLon(
                    location.latitude.toFloat(),
                    location.longitude.toFloat()
                )
                navigateToHomeFragment(location)
            }
            .setNegativeButton("No Thanks") { _, _ ->
                navigateToHomeFragment(location)
            }
        builder.create().show()
    }
}