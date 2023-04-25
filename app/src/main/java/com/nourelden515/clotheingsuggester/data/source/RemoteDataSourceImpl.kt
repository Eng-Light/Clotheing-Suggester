package com.nourelden515.clotheingsuggester.data.source

import com.google.gson.Gson
import com.nourelden515.clotheingsuggester.BuildConfig
import com.nourelden515.clotheingsuggester.data.models.WeatherResponse
import io.reactivex.rxjava3.core.Observable
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

class RemoteDataSourceImpl() : RemoteDataSource {
    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    override fun getWeatherData(
        lat: Float,
        lon: Float
    ):Observable<Response> {
        val url = "https://api.weatherapi.com/v1/current.json"
        val formRequestBody = FormBody.Builder()
            .add("key", BuildConfig.API_KEY)
            .add("q", "${lat},${lon}")
            .build()
        val request = Request.Builder()
            .url(url)
            .post(formRequestBody)
            .build()
        val weatherObservable = Observable.create { emitter ->
            try {
                val response1 = client.newCall(request).execute()
                val result1 = Gson().fromJson(response1.body?.string(), WeatherResponse::class.java)
                val formRequestBody2 = FormBody.Builder()
                    .add("key", BuildConfig.API_KEY)
                    .add("q", result1.location.name)
                    .build()
                val request2 = Request.Builder()
                    .url(url)
                    .post(formRequestBody2)
                    .build()
                val response2 = client.newCall(request2).execute()
                emitter.onNext(response2)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
        return weatherObservable
    }
}
