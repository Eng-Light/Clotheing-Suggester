package com.nourelden515.clotheingsuggester.data.models

import com.google.gson.annotations.SerializedName

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
