package com.nourelden515.clotheingsuggester.ui.home

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nourelden515.clotheingsuggester.base.BaseViewModel
import com.nourelden515.clotheingsuggester.data.Repository
import com.nourelden515.clotheingsuggester.data.models.ApiState
import com.nourelden515.clotheingsuggester.data.models.WeatherResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Calendar
import java.util.Random

class HomeViewModel(private val repository: Repository) : BaseViewModel() {

    override val tag: String
        get() = "HomeViewModel"

    private val _weatherResponse = MutableLiveData<ApiState<WeatherResponse>>()
    val weatherResponse: LiveData<ApiState<WeatherResponse>>
        get() = _weatherResponse

    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse>
        get() = _weatherData

    private val _imageIndex = MutableLiveData<Int>()
    val imageIndex: LiveData<Int>
        get() = _imageIndex

    private val _imageLastViewedDay = MutableLiveData<Int>()
    val imageLastViewedDay: LiveData<Int>
        get() = _imageLastViewedDay

    private val _outfitImageDrawable = MutableLiveData<Drawable>()
    val outfitImageDrawable: LiveData<Drawable>
        get() = _outfitImageDrawable

    fun getWeatherData(lat: Double, lon: Double) {
        _weatherResponse.value = ApiState.Loading
        val resultDisposable = repository.getWeatherData(
            lat.toFloat(),
            lon.toFloat()
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                ::onGetWeatherDataSuccess,
                ::onGetWeatherDataFailure
            )
        addToCompositeDisposable(resultDisposable)
    }

    private fun onGetWeatherDataSuccess(weatherResponse: WeatherResponse) {
        _weatherResponse.value = ApiState.Success(weatherResponse)
        _weatherData.value = _weatherResponse.value?.toData()
        setOutfitImage()
    }

    private fun setOutfitImage() {
        val outFits = getOutfits()
        val today = getToday()
        if (imageIndex.value == -1 || today != imageLastViewedDay.value) {
            setNewImage(outFits, today)
        } else {
            setSameImage(outFits)
        }
    }

    fun skip() {
        val outFits = getOutfits()
        val today = getToday()
        setNewImage(outFits, today)
    }

    private fun getOutfits(): List<Drawable> {
        val temp = weatherData.value!!.currentWeather.temperatureCelsius.toInt()
        return repository.getOutfits(temp)
    }

    private fun setSameImage(outFits: List<Drawable>) {
        val savedImage = outFits[imageIndex.value!!]
        _outfitImageDrawable.postValue(savedImage)
    }

    private fun setNewImage(
        outFits: List<Drawable>,
        today: Int
    ) {
        val newImageIndex = Random().nextInt(outFits.size)
        val newImage = outFits[newImageIndex]

        saveImageData(newImageIndex, today)
        setImageDrawable(newImage)
    }

    private fun getToday(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_YEAR)
    }

    private fun onGetWeatherDataFailure(error: Throwable) {
        _weatherResponse.value = ApiState.Error(error)
        log(error.message.toString())
    }

    private fun saveImageData(newImageIndex: Int, today: Int) {
        repository.saveImageIndex(newImageIndex)
        repository.saveToday(today)
    }

    fun getImageIndex() {
        _imageIndex.postValue(repository.getImageIndex())
    }

    fun getImageLastViewedDay() {
        _imageLastViewedDay.postValue(repository.getLastViewedDay())
    }

    fun resetLocation() {
        repository.clearLatLon()
    }

    private fun setImageDrawable(newImage: Drawable) {
        _outfitImageDrawable.postValue(newImage)
    }
}