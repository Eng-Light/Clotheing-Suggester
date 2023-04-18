package com.nourelden515.clotheingsuggester.data.models

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("location") val location: Location,
    @SerializedName("current") val currentWeather: CurrentWeather
) {
    data class Location(
        @SerializedName("name") val name: String,
        @SerializedName("region") val region: String,
        @SerializedName("country") val country: String,
        @SerializedName("lat") val latitude: Double,
        @SerializedName("lon") val longitude: Double,
        @SerializedName("tz_id") val timeZoneId: String,
        @SerializedName("localtime_epoch") val localTimeEpoch: Long,
        @SerializedName("localtime") val localTime: String
    )

    data class CurrentWeather(
        @SerializedName("last_updated_epoch") val lastUpdatedEpoch: Long,
        @SerializedName("last_updated") val lastUpdated: String,
        @SerializedName("temp_c") val temperatureCelsius: Double,
        @SerializedName("temp_f") val temperatureFahrenheit: Double,
        @SerializedName("is_day") val isDay: Int,
        @SerializedName("condition") val condition: Condition,
        @SerializedName("wind_mph") val windSpeedMph: Double,
        @SerializedName("wind_kph") val windSpeedKph: Double,
        @SerializedName("wind_degree") val windDegree: Int,
        @SerializedName("wind_dir") val windDirection: String,
        @SerializedName("pressure_mb") val pressureMb: Double,
        @SerializedName("pressure_in") val pressureIn: Double,
        @SerializedName("precip_mm") val precipitationMm: Double,
        @SerializedName("precip_in") val precipitationIn: Double,
        @SerializedName("humidity") val humidity: Int,
        @SerializedName("cloud") val cloud: Int,
        @SerializedName("feelslike_c") val feelsLikeTemperatureCelsius: Double,
        @SerializedName("feelslike_f") val feelsLikeTemperatureFahrenheit: Double,
        @SerializedName("vis_km") val visibilityKm: Double,
        @SerializedName("vis_miles") val visibilityMiles: Double,
        @SerializedName("uv") val uvIndex: Double,
        @SerializedName("gust_mph") val windGustMph: Double,
        @SerializedName("gust_kph") val windGustKph: Double
    )

    data class Condition(
        @SerializedName("text") val text: String,
        @SerializedName("icon") val iconUrl: String,
        @SerializedName("code") val code: Int
    )
}