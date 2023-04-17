package com.nourelden515.clotheingsuggester.ui.home

import android.os.Bundle
import com.nourelden515.clotheingsuggester.base.BaseFragment
import com.nourelden515.clotheingsuggester.data.RepositoryImpl
import com.nourelden515.clotheingsuggester.data.models.WeatherResponse
import com.nourelden515.clotheingsuggester.data.source.RemoteDataSourceImpl
import com.nourelden515.clotheingsuggester.databinding.FragmentHomeBinding
import com.nourelden515.clotheingsuggester.utils.SharedPreferencesUtils

class HomeFragment : BaseFragment<FragmentHomeBinding>(), HomeView {
    private lateinit var location: Pair<Double, Double>

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
        if (::location.isInitialized) {
            presenter.getWeatherData(location.first, location.second)
        }
    }

    override fun showWeatherData(weatherData: WeatherResponse) {
        log(weatherData.toString())
    }

    override fun showError(error: Throwable) {
        TODO("Not yet implemented")
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