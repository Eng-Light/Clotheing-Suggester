package com.nourelden515.clotheingsuggester.data.models

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("coord") val coord: Coordinates,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("base") val base: String,
    @SerializedName("main") val main: Main,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("clouds") val clouds: Clouds,
    @SerializedName("dt") val dateTime: Long,
    @SerializedName("sys") val sys: Sys,
    @SerializedName("timezone") val timezone: Int,
    @SerializedName("id") val cityId: Int,
    @SerializedName("name") val cityName: String,
    @SerializedName("cod") val cityCod: Int
)

data class Coordinates(
    @SerializedName("lon") val lon: Float,
    @SerializedName("lat") val lat: Float
)

data class Weather(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class Main(
    @SerializedName("temp") val temperature: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val minTemperature: Double,
    @SerializedName("temp_max") val maxTemperature: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("sea_level") val seaLevelPressure: Int,
    @SerializedName("grnd_level") val groundLevelPressure: Int
)

data class Wind(
    @SerializedName("speed") val speed: Double,
    @SerializedName("deg") val direction: Int,
    @SerializedName("gust") val gust: Double
)

data class Clouds(
    @SerializedName("all") val cloudinessPercentage: Int
)

data class Sys(
    @SerializedName("type") val type: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("country") val country: String,
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long
)