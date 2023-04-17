package com.nourelden515.clotheingsuggester.ui.home

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.nourelden515.clotheingsuggester.base.BaseFragment
import com.nourelden515.clotheingsuggester.data.RepositoryImpl
import com.nourelden515.clotheingsuggester.data.models.WeatherResponse
import com.nourelden515.clotheingsuggester.data.source.RemoteDataSourceImpl
import com.nourelden515.clotheingsuggester.databinding.FragmentHomeBinding
import com.nourelden515.clotheingsuggester.utils.SharedPreferencesUtils
import com.nourelden515.clotheingsuggester.utils.getSummerOutfits
import com.nourelden515.clotheingsuggester.utils.getWinterOutfits
import java.util.Calendar
import java.util.Random
import kotlin.properties.Delegates

class HomeFragment : BaseFragment<FragmentHomeBinding>(), HomeView {
    private lateinit var location: Pair<Double, Double>
    private var temp by Delegates.notNull<Int>()
    private var imageIndex by Delegates.notNull<Int>()
    private var lastViewedDay by Delegates.notNull<Int>()

    private val presenter by lazy {
        HomePresenter(
            RepositoryImpl(
                RemoteDataSourceImpl(),
                SharedPreferencesUtils(requireContext())
            ), this
        )
    }

    override val TAG = this::class.java.simpleName.toString()
    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun setUp() {
        getLocation()
        refreshApp()
        if (::location.isInitialized) {
            binding.loading.visibility = View.VISIBLE
            presenter.getImageData()
            presenter.getWeatherData(location.first, location.second)
        }
    }

    private fun setSkipListener() {
        binding.buttonSkip.setOnClickListener {
            val outFits = getOutfits()
            val today = getToday()
            val newImageIndex = Random().nextInt(outFits.size)
            val newImage = outFits[newImageIndex]
            presenter.saveImageData(newImageIndex, today)
            binding.imageOutfit.setImageDrawable(newImage)
        }
    }

    private fun refreshApp() {
        binding.swipeToRefresh.setOnRefreshListener {
            binding.loading.visibility = View.VISIBLE
            presenter.getWeatherData(location.first, location.second)
            binding.swipeToRefresh.isRefreshing = false
        }
    }

    override fun showWeatherData(weatherData: WeatherResponse) {
        log(weatherData.toString())
        temp = weatherData.currentWeather.temperatureCelsius.toInt()
        activity?.runOnUiThread {
            binding.loading.visibility = View.INVISIBLE
            "${weatherData.location.name}, ${weatherData.location.region}".also {
                binding.textLocation.text = it
            }
            with(binding.weatherCard) {
                "${temp}°".also {
                    textTemp.text = it
                }
                textCondition.text = weatherData.currentWeather.condition.text
                textDateTime.text = weatherData.location.localTime
                "Feels Like ${
                    weatherData
                        .currentWeather
                        .feelsLikeTemperatureCelsius.toInt()
                }°".also {
                    textFeelsLike.text = it
                }
            }
            Glide
                .with(binding.weatherCard.imageWeather.context)
                .load("https:/${weatherData.currentWeather.condition.iconUrl}")
                .into(binding.weatherCard.imageWeather)
            setOutfitImage()
        }
    }

    override fun showError(error: Throwable) {
        activity?.runOnUiThread {
            binding.loading.visibility = View.INVISIBLE
            Toast.makeText(requireContext(), "Connection Faild $error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setOutfitImage() {
        val outFits = getOutfits()
        val today = getToday()
        if (imageIndex == -1 || today != lastViewedDay) {
            val newImageIndex = Random().nextInt(outFits.size)
            val newImage = outFits[newImageIndex]
            presenter.saveImageData(newImageIndex, today)

            binding.imageOutfit.setImageDrawable(newImage)
        } else {
            val savedImage = outFits[imageIndex]
            binding.imageOutfit.setImageDrawable(savedImage)
        }
        setSkipListener()
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