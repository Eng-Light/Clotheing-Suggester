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

    private val logInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(logInterceptor)
    }.build()

    override fun getWeatherData(
        lat: Float, lon: Float,
        observable: (Observable<Response>) -> Unit
    ) {
        val formRequest1Body = FormBody.Builder()
            .add("key", BuildConfig.API_KEY)
            .add("q", "${lat},${lon}")
            .build()

        val request1 = Request.Builder()
            .url(url)
            .post(formRequest1Body)
            .build()
        //val responseType = object : TypeToken<WeatherResponse>() {}.type

        val observer1 = Observable.create { emitter ->
            try {
                val response = client.newCall(request1).execute()
                emitter.onNext(response)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }

        val observable2 = observer1.flatMap { emitter ->
            val responseBody = emitter.body?.string()
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

            Observable.create<Response> { emitter ->
                try {
                    val response2 = client.newCall(request2).execute()
                    emitter.onNext(response2)
                    emitter.onComplete()
                } catch (e: Exception) {
                    emitter.onError(e)
                }
            }
        }
        observable(observable2)
    }

    companion object {
        const val url = "https://api.weatherapi.com/v1/current.json"
    }
}