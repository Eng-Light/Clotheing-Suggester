package com.nourelden515.clotheingsuggester.ui.home

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.nourelden515.clotheingsuggester.data.Repository
import com.nourelden515.clotheingsuggester.data.models.WeatherResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Response

class HomePresenter(
    private val repository: Repository,
    private val homeView: HomeView
) {
    fun getWeatherData(lat: Double, lon: Double) {
        observeResponse(
            repository.getWeatherData(
                lat.toFloat(),
                lon.toFloat()
            )
        )
    }

    @SuppressLint("CheckResult")
    private fun observeResponse(observable: Single<Response>) {
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val responseBody = it.body?.string()
                val gson = Gson()
                val result = gson.fromJson(responseBody, WeatherResponse::class.java)
                onGetWeatherDataSuccess(result)
            }, {
                onGetWeatherDataFailure(it)
            })
    }

    private fun onGetWeatherDataSuccess(response: WeatherResponse) {
        homeView.showWeatherData(response)
    }

    private fun onGetWeatherDataFailure(error: Throwable) {
        homeView.showError(error)
    }

    fun resetLocation() {
        repository.clearLatLon()
    }

    fun saveImageData(newImageIndex: Int, today: Int) {
        repository.saveImageIndex(newImageIndex)
        repository.saveToday(today)
    }

    fun getImageData() {
        homeView.getImageData(repository.getImageIndex(), repository.getLastViewedDay())
    }

}