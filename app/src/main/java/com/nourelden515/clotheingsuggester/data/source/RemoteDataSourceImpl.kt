package com.nourelden515.clotheingsuggester.data.source

import com.google.gson.reflect.TypeToken
import com.nourelden515.clotheingsuggester.BuildConfig
import com.nourelden515.clotheingsuggester.data.models.WeatherResponse
import com.nourelden515.clotheingsuggester.utils.executeWithCallbacks
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

class RemoteDataSourceImpl() : RemoteDataSource {

    private val logInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(logInterceptor)
    }.build()

    override fun getWeatherData(
        lat: Float, lon: Float,
        onSuccess: (response: WeatherResponse) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val formBody = FormBody.Builder()
            .add("key", BuildConfig.API_KEY)
            .add("q", "${lat},${lon}")
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        val responseType = object : TypeToken<WeatherResponse>() {}.type

        client.executeWithCallbacks(
            request,
            responseType,
            onSuccess,
            onFailure
        )

    }

    companion object {
        const val url = "https://api.weatherapi.com/v1/current.json"
    }
}