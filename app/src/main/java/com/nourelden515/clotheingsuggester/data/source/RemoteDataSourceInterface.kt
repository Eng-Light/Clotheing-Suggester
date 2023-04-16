package com.nourelden515.clotheingsuggester.data.source

import com.nourelden515.clotheingsuggester.data.models.WeatherResponse

interface RemoteDataSourceInterface {
    fun getWeatherData(
        lat: Float, lon: Float,
        onSuccessCallback: (response: WeatherResponse) -> Unit,
        onFailureCallback: (error: Throwable) -> Unit
    )
}