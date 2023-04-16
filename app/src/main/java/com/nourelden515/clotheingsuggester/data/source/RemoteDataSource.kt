package com.nourelden515.clotheingsuggester.data.source

import com.google.gson.reflect.TypeToken
import com.nourelden515.clotheingsuggester.BuildConfig
import com.nourelden515.clotheingsuggester.data.models.WeatherResponse
import com.nourelden515.clotheingsuggester.utils.executeWithCallbacks
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

class RemoteDataSource() : RemoteDataSourceInterface {

    private val logInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(logInterceptor)
    }.build()

    override fun getWeatherData(
        lat: Float, lon: Float,
        onSuccessCallback: (response: WeatherResponse) -> Unit,
        onFailureCallback: (error: Throwable) -> Unit
    ) {
        val formBody = FormBody.Builder()
            .add("lat", lat.toString())
            .add("lon", lon.toString())
            .add("appid", BuildConfig.API_KEY)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        val responseType = object : TypeToken<WeatherResponse>() {}.type

        client.executeWithCallbacks(
            request,
            responseType,
            onSuccessCallback,
            onFailureCallback
        )

    }

    companion object {
        const val url = "https://api.openweathermap.org/data/2.5/weather"
    }
}