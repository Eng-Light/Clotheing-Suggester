package com.nourelden515.clotheingsuggester.data.source

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import okhttp3.Response

interface RemoteDataSource {
    fun getWeatherData(
        lat: Float, lon: Float
    ): Single<Response>
}