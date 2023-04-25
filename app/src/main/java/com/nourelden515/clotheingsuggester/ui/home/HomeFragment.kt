package com.nourelden515.clotheingsuggester.ui.home

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.nourelden515.clotheingsuggester.R
import com.nourelden515.clotheingsuggester.base.BaseFragment
import com.nourelden515.clotheingsuggester.data.Repository
import com.nourelden515.clotheingsuggester.data.RepositoryImpl
import com.nourelden515.clotheingsuggester.data.models.WeatherResponse
import com.nourelden515.clotheingsuggester.data.source.RemoteDataSourceImpl
import com.nourelden515.clotheingsuggester.databinding.FragmentHomeBinding
import com.nourelden515.clotheingsuggester.ui.location.LocationFragment
import com.nourelden515.clotheingsuggester.utils.getSummerOutfits
import com.nourelden515.clotheingsuggester.utils.getWinterOutfits
import com.nourelden515.clotheingsuggester.utils.onClickBackFromNavigation
import com.nourelden515.clotheingsuggester.utils.replaceFragment
import com.nourelden515.clotheingsuggester.utils.shared.SharedPreferencesInterface
import com.nourelden515.clotheingsuggester.utils.shared.SharedPreferencesUtils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale
import java.util.Random
import kotlin.properties.Delegates

class HomeFragment : BaseFragment<FragmentHomeBinding>(), HomeView {
    private lateinit var location: Pair<Double, Double>
    private var temp by Delegates.notNull<Int>()
    private var imageIndex by Delegates.notNull<Int>()
    private var lastViewedDay by Delegates.notNull<Int>()

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
        HomePresenter(
            repository, this
        )
    }

    override val TAG = this::class.java.simpleName.toString()
    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun setUp() {
        getLocation()
        refreshApp()
        checkLocationAndGetAllData()
        addCallbacks()
        onClickBackFromNavigation()
    }

    private fun addCallbacks() {
        binding.buttonResetLocation.setOnClickListener {
            showResetLocationDialog()
        }
    }

    private fun setSkipListener() {
        binding.buttonSkip.setOnClickListener {
            val outFits = getOutfits()
            val today = getToday()
            setNewImage(outFits, today)
        }
    }

    private fun refreshApp() {
        binding.swipeToRefresh.setOnRefreshListener {
            checkLocationAndGetAllData()
            binding.swipeToRefresh.isRefreshing = false
        }
    }

    private fun showLoading() {
        binding.loading.visibility = View.VISIBLE
    }

    override fun showWeatherData(weatherData: WeatherResponse) {
        log(weatherData.toString())
        temp = weatherData.currentWeather.temperatureCelsius.toInt()
        activity?.runOnUiThread {
            stopLoading()
            showLocation(weatherData)
            showDataInWeatherCard(weatherData)
            loadImage(weatherData.currentWeather.condition.iconUrl)
            setOutfitImage()
        }
    }

    private fun showLocation(weatherData: WeatherResponse) {
        "${weatherData.location.name}, ${weatherData.location.region}".also {
            binding.textLocation.text = it
        }
    }

    private fun showDataInWeatherCard(weatherData: WeatherResponse) {
        with(binding.weatherCard) {
            "${temp}°C".also {
                textTemp.text = it
            }
            textCondition.text = weatherData.currentWeather.condition.text
            textDateTime.text = convertDateFormat(weatherData.location.localTime)
            "Feels Like ${
                weatherData
                    .currentWeather
                    .feelsLikeTemperatureCelsius.toInt()
            }°C".also {
                textFeelsLike.text = it
            }
        }
    }

    private fun stopLoading() {
        binding.loading.visibility = View.INVISIBLE
    }

    private fun loadImage(iconUrl: String) {
        Glide
            .with(binding.weatherCard.imageWeather.context)
            .load("https:/${iconUrl}")
            .into(binding.weatherCard.imageWeather)
    }

    override fun showError(error: Throwable) {
        activity?.runOnUiThread {
            stopLoading()
            showSnackBar(getString(R.string.connection_error)) {
                checkLocationAndGetAllData()
            }
        }
    }

    private fun checkLocationAndGetAllData() {
        if (::location.isInitialized) {
            showLoading()
            presenter.getImageData()
            presenter.getWeatherData(location.first, location.second)
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

    private fun setOutfitImage() {
        val outFits = getOutfits()
        val today = getToday()
        if (imageIndex == -1 || today != lastViewedDay) {
            setNewImage(outFits, today)
        } else {
            setSameImage(outFits)
        }
        setSkipListener()
    }

    private fun setSameImage(outFits: List<Drawable>) {
        val savedImage = outFits[imageIndex]
        binding.imageOutfit.setImageDrawable(savedImage)
    }

    private fun setNewImage(
        outFits: List<Drawable>,
        today: Int
    ) {
        val newImageIndex = Random().nextInt(outFits.size)
        val newImage = outFits[newImageIndex]
        presenter.saveImageData(newImageIndex, today)

        binding.imageOutfit.setImageDrawable(newImage)
        log(imageIndex)
    }

    private fun getToday(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_YEAR)
    }

    override fun getImageData(imageIndex: Int, lastViewedDay: Int) {
        this.imageIndex = imageIndex
        this.lastViewedDay = lastViewedDay
    }

    private fun getOutfits(): List<Drawable> {
        return if (temp >= 20) {
            getSummerOutfits()
        } else {
            getWinterOutfits()
        }
    }

    private fun getLocation() {
        arguments?.let {
            location = Pair(it.getDouble(LATITUDE), it.getDouble(LONGITUDE))
        }
    }

    private fun convertDateFormat(dateStr: String): String {
        val formatter = DateTimeFormatter.ofPattern(getString(R.string.yyyy_mm_dd_h_mm))
        val dateTime = LocalDateTime.parse(dateStr, formatter)

        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val time = dateTime.format(DateTimeFormatter.ofPattern(getString(R.string.h_mm_a)))

        return "$dayOfWeek, $time"
    }

    private fun showResetLocationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_title))
            .setMessage(getString(R.string.accept))
            .setPositiveButton(
                getString(R.string.yes)
            ) { _, _ ->
                presenter.resetLocation()
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
        replaceFragment(LocationFragment())
    }

    companion object {
        const val LATITUDE = "LATITUDE"
        const val LONGITUDE = "LONGITUDE"

        fun newInstance(latitude: Double, longitude: Double) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putDouble(LATITUDE, latitude)
                    putDouble(LONGITUDE, longitude)
                }
            }
    }

}