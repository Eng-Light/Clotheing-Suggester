package com.nourelden515.clotheingsuggester.ui.home

import com.google.gson.Gson
import com.nourelden515.clotheingsuggester.data.Repository
import com.nourelden515.clotheingsuggester.data.models.WeatherResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
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

    private fun observeResponse(observable: Observable<Response>) {
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response> {
                override fun onSubscribe(d: Disposable) {
                    //TODO("Not yet implemented")
                }

                override fun onError(e: Throwable) {
                    onGetWeatherDataFailure(e)
                }

                override fun onComplete() {
                    //TODO("Not yet implemented")
                }

                override fun onNext(response: Response) {
                    val responseBody = response.body?.string()
                    val gson = Gson()
                    val result = gson.fromJson(responseBody, WeatherResponse::class.java)
                    onGetWeatherDataSuccess(result)
                }

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