package com.nourelden515.clotheingsuggester.data.source

import com.google.gson.Gson
import com.nourelden515.clotheingsuggester.BuildConfig
import com.nourelden515.clotheingsuggester.data.models.WeatherResponse
import io.reactivex.rxjava3.core.Single
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

class RemoteDataSourceImpl : RemoteDataSource {

    private val logInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(logInterceptor)
    }.build()

    override fun getWeatherData(
        lat: Float, lon: Float
    ): Single<Response> {
        val formRequest1Body = FormBody.Builder()
            .add("key", BuildConfig.API_KEY)
            .add("q", "${lat},${lon}")
            .build()

        val request1 = Request.Builder()
            .url(url)
            .post(formRequest1Body)
            .build()

        return Single.create { emitter ->
            try {
                val response = client.newCall(request1).execute()
                emitter.onSuccess(response)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }.flatMap { response1 ->
            val responseBody = response1.body?.string()
            val gson = Gson()
            val result = gson.fromJson(responseBody, WeatherResponse::class.java)

            val formRequest2Body = FormBody.Builder()
                .add("key", BuildConfig.API_KEY)
                .add("q", result.location.name)
                .build()

            val request2 = Request.Builder()
                .url(url)
                .post(formRequest2Body)
                .build()

            Single.create { emitter ->
                try {
                    val response2 = client.newCall(request2).execute()
                    emitter.onSuccess(response2)
                } catch (e: Exception) {
                    emitter.onError(e)
                }
            }
        }
    }

    companion object {
        const val url = "https://api.weatherapi.com/v1/current.json"
    }
}