package com.nourelden515.clotheingsuggester.data.models

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("location") val location: Location,
    @SerializedName("current") val currentWeather: CurrentWeather
)